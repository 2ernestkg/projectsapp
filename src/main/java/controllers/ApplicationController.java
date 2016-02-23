package controllers;

import com.google.inject.Singleton;

import ninja.Result;
import ninja.Results;

@Singleton
public class ApplicationController {
	public Result index(){
		return Results.html();
	}
}
