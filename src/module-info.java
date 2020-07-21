module calculator 
{
	requires javafx.base;
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;
	requires transitive javafx.media;
	requires javafx.swing;
	requires javafx.web;
	requires com.google.gson;
	
	exports calculator;
	exports item;
	exports utils;
}