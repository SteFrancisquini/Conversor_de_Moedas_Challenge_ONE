import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClienteExchangeRate {
    private static final String urlBase = "https://v6.exchangerate-api.com/v6/fd242a7b14f225ceb76abe82";

    public static List<List<String>> obterMoedasSuportadas() {
        URI uriMoedas = URI.create(urlBase + "/codes");
        List<String> codigosSuportados = Arrays.asList("ARS", "BOB", "BRL", "CLP", "COP", "USD");
        List<List<String>> moedasSuportadas = new ArrayList<>();
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder().uri(uriMoedas).build();
            HttpResponse<String> httpResponse = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (httpResponse.statusCode() == 200) {
                String json = httpResponse.body();

                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(json, JsonObject.class);

                JsonArray supportedCodes = jsonObject.get("supported_codes").getAsJsonArray();

                for (JsonElement element : supportedCodes) {
                    JsonArray moedaArray = element.getAsJsonArray();
                    String codigo = moedaArray.get(0).getAsString();
                    String nome = moedaArray.get(1).getAsString();

                    if (codigosSuportados.contains(codigo)) {
                        List<String> moeda = new ArrayList<>();
                        moeda.add(codigo);
                        moeda.add(nome);
                        moedasSuportadas.add(moeda);
                    }
                }
                return moedasSuportadas;
            } else {
                throw new RuntimeException("Resposta HTTP inválida: " + httpResponse.statusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível obter as moedas suportadas", e);
        }
    }

    public static double converter(double valor, String moedaOrigem, String moedaDestino) {
        URI uriConversao = URI.create(urlBase + "/latest/" + moedaOrigem);
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder().uri(uriConversao).build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                String json = response.body();

                Gson gson = new Gson();
                JsonObject object = gson.fromJson(json, JsonObject.class);


                JsonObject taxas = object.get("conversion_rates").getAsJsonObject();
                if (!taxas.has(moedaDestino)) {
                    throw new RuntimeException("Moeda de destino não suportada!");
                }
                double taxaCambio = taxas.get(moedaDestino).getAsDouble();
                return valor * taxaCambio;
            } else {
                throw new RuntimeException("Resposta HTTP inválida: " + response.statusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao converter moedas. Verifique os códigos informados.", e);
        }
    }
}
