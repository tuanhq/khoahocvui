package com.vas.aps.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class MoSyntax implements Serializable {

	private ArrayList<String> listSyntax;
	private String command;

	public MoSyntax() {
		// TODO Auto-generated constructor stub
	}

	public MoSyntax(ArrayList<String> listSyntax, String command) {
		super();
		this.listSyntax = listSyntax;
		this.command = command;
	}

	public ArrayList<String> getListSyntax() {
		return listSyntax;
	}

	public void setListSyntax(ArrayList<String> listSyntax) {
		this.listSyntax = listSyntax;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}
}
