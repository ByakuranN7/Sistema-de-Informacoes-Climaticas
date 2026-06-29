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

    }
}



