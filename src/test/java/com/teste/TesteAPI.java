package com.teste;

import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static com.teste.massaDados.*;

public class TesteAPI {
	private String vote_id;
	private massaDados massa = new massaDados();
	
	@BeforeClass
	public static void urlbase() {
		RestAssured.baseURI = "https://api.thecatapi.com/v1/";
	}

	@Test
	public void cadastro() {
		given().contentType("application/json").body("{\"email\": \"fabiojcbweb00@hotmail.com\", \"appDescription\": \"teste the cat api\"}").when().post("user/passwordlesssignup");
		//validacao(response);
	}

	@Test
	public void votacao() {
		Response response = given().contentType("application/json").body(massa.corpoVotacao).when().post("votes/");
		validacao(response);
		String id = response.jsonPath().getString("id");
		massa.vote_id = id;
		System.out.println("ID => " + id);

	}

	@Test
	public void deletaVotacao() {
		votacao();
		delataVoto();
	}

	private void delataVoto() {

		Response response = given().contentType("application/json")
				.header("x-api-key", "444e1b0f-9c65-4774-bd94-1293fc76f192").pathParam("vote_id", massa.vote_id).when()
				.delete("votes/{vote_id}");
		validacao(response);
	}

	@Test
	public void favoritaDesfavorita() {
		favorita();
		desfavorita();
	}

	private void favorita() {
		Response response = given().contentType("application/json")
				.header("x-api-key", "444e1b0f-9c65-4774-bd94-1293fc76f192").body(massa.corpoFavorita).when()
				.post("favourites");
		String id = response.jsonPath().getString("id");
		massa.vote_id = id;
		validacao(response);
	}

	private void desfavorita() {
		Response response = given().contentType("application/json")
				.header("x-api-key", "444e1b0f-9c65-4774-bd94-1293fc76f192").pathParam("favourite_id", vote_id).when()
				.delete(massa.corpoDesfavorita);
		validacao(response);

	}

	public void validacao(Response response) {

		response.then().statusCode(200).body("message", containsString("SUCCESS"));
		System.out.println("RETORNO DA API => " + response.body().asString());
		System.out.println("-----------------------------------------------");
	}
	
}
