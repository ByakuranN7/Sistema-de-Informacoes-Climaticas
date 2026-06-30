//Documentação da WeatherAPI disponivel em https://www.weatherapi.com/docs/

import org.json.JSONObject;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner; //remover futuramente ao implementar interface

public class WeatherApplication{

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Digite o nome da cidade: ");
        String cidade = scanner.nextLine();

        try{
            String dadosClimaticos = getDadosClimaticos(cidade); //retorna um json

            // Código 1006 significa localização não encontrada
            if(dadosClimaticos.contains("\"code\":1006")){ //"code":1006
                System.out.println("Localização não encontrada. Por favor, tente novamente.");
            }else{
                imprimirDadosClimaticos(dadosClimaticos);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static String getDadosClimaticos(String cidade) throws Exception{
        String apiKey = Files.readString(Paths.get("api-key.txt")).trim();

        String formatNomeCidade = URLEncoder.encode(cidade, StandardCharsets.UTF_8);
        String apiUrl = "http://api.weatherapi.com/v1/current.json?key=" + apiKey + "&q=" + formatNomeCidade;
        HttpRequest request = HttpRequest.newBuilder() //Começa a construção de uma nova solicitação http
                .uri(URI.create(apiUrl)) //Este método define a uri da solicitação http
                .build(); //finaliza a construção da solicitação

        //cria objeto para enviar solicitações e receber respostas
        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body(); //retorna os dados meteorológicos obtidos no site da API
    }

    public static void imprimirDadosClimaticos(String dadosClimaticos){
        //System.out.println("Dados originais em JSON" + dadosClimaticos);

        JSONObject dadosJson = new JSONObject(dadosClimaticos);
        JSONObject informacoesMeteorologicas = dadosJson.getJSONObject("current"); //pegando os dados atuais

        //Extrai os dados da cidade informada
        String cidade = dadosJson.getJSONObject("location").getString("name");
        String pais = dadosJson.getJSONObject("location").getString("country");

        //Extrai dados adicionais da localização
        String condicaoTempo = informacoesMeteorologicas.getJSONObject("condition").getString("text");
        int umidade = informacoesMeteorologicas.getInt("humidity");
        float velocidadeVento = informacoesMeteorologicas.getFloat("wind_kph");
        float pressaoAtmosferica = informacoesMeteorologicas.getFloat("pressure_mb");
        float sensacaoTermica = informacoesMeteorologicas.getFloat("feelslike_c");
        float temperaturaAtual = informacoesMeteorologicas.getFloat("temp_c");

        //Extrai a data e hora da string retornada pela API
        String datahoraString = informacoesMeteorologicas.getString("last_updated");

        System.out.println("Informações Meteorológicas para: " + cidade + ", " + pais);
        System.out.println("Data e Hora: " + datahoraString);
        System.out.println("Temperatura Atual: " + temperaturaAtual + " °C");
        System.out.println("Sensação Térmica: " + sensacaoTermica + " °C");
        System.out.println("Condição do Tempo: " + condicaoTempo);
        System.out.println("Umidade " + umidade + "%");
        System.out.println("Velocidade do Vento: " + velocidadeVento + "km/h");
        System.out.println("Pressão Atmosférica: " + pressaoAtmosferica + "mb");
    }
}



