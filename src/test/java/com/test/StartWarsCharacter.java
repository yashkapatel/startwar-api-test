package com.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.mapper.TypeRef;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;
import static io.restassured.module.jsv.JsonSchemaValidator.*;
import static io.restassured.path.json.JsonPath.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StartWarsCharacter {

	@Test
	public void testStatusCode() {
		given().get("https://swapi.co/api/").then().statusCode(200);
	}

	 @Test
	public void testlogALL() {
		given().get("https://swapi.co/api/people").then().statusCode(200).log().all();
	}

	 @Test
	public void testResponse() {
		given().get("https://swapi.co/api/people").then().body("results[0].name", equalTo("Luke Skywalker"));
	}

	@Test
	public void testResponse1() {
		given().get("https://swapi.co/api/people").then().body("results.name",
				hasItems("Luke Skywalker", "R2-D2", "Darth Vader", "Leia Organa", "Owen Lars"));
	}

	@Test
	public void returnListOfplanet() {
		// type 1
		Response response = get("https://swapi.co/api/people");
		JsonPath jsonPathEvaluator = response.jsonPath();
		List<String> allPlanetss = jsonPathEvaluator.getList("results.name");
		for (String planet : allPlanetss) {
			System.out.println("planet: " + planet);
		}
		// type 2
		String response1 = get("https://swapi.co/api/people").asString();
		List<String> bookTitles = from(response1).getList("results.name");
		for (String planet : bookTitles) {
			System.out.println("planet: " + planet);
		}
		// type 3
		List<String> myplanet = get("https://swapi.co/api/people").path("results.name");
		for (String planet : myplanet) {
			System.out.println("planet: " + planet);
		}
	}

	 @Test
	public void returnAllListOfActors() {

		String nextLink = "https://swapi.co/api/people";
		List<String> allActors = new ArrayList<String>();

		do {
			String currentResponse = get(nextLink).asString();
			List<String> currentPageplanets = from(currentResponse).getList("results.name");
			nextLink = from(currentResponse).getString("next");
			allActors.addAll(currentPageplanets);

		} while (nextLink != null);

		for (String Actor : allActors) {
			System.out.println("Actor: " + Actor);
		}

	}

	 @Test
	public void returnAllListPlanets() {

		String nextLink = "https://swapi.co/api/planets";
		List<String> allPlanets = new ArrayList<String>();

		do {
			String currentResponse = get(nextLink).asString();
			List<String> currentPageplanets = from(currentResponse).getList("results.name");
			nextLink = from(currentResponse).getString("next");
			allPlanets.addAll(currentPageplanets);

		} while (nextLink != null);

		for (String planet : allPlanets) {
			System.out.println("Planet: " + planet);
		}

	}

	@Test
	public void specifiedStarWarPlanets() {

		List<Map<String, Object>> response = returnSpecifiedPlanets("Yavin IV");
		System.out.println(response);
	}

	public List<Map<String, Object>> returnSpecifiedPlanets(String PlanetName) {

		String nextLink = "https://swapi.co/api/planets/";
		do {
			String response = get(nextLink).asString();
			nextLink = from(response).getString("next");
			List<Map<String, Object>> objects = from(response).getList("results");
			for (int i = 0; i < objects.size(); i++) {
				if (objects.get(i).get("name").equals(PlanetName)) {
					return objects.subList(i, i + 1);
				}
			}
		} while (nextLink != null);
		return null;
	}
	
	@Test
	public void specifiedStarWarCharacter() {

		List<Map<String, Object>> response = returnSpecifiedCharacter("Luke Skywalker");
		System.out.println(response);
	}
	
	public List<Map<String, Object>> returnSpecifiedCharacter(String charName) {

		String nextLink = "https://swapi.co/api/people/";
		do {
			String response = get(nextLink).asString();
			nextLink = from(response).getString("next");
			List<Map<String, Object>> objects = from(response).getList("results");
			for (int i = 0; i < objects.size(); i++) {
				if (objects.get(i).get("name").equals(charName)) {
					return objects.subList(i, i + 1);
				}
			}
		} while (nextLink != null);
		return null;
	}
}
