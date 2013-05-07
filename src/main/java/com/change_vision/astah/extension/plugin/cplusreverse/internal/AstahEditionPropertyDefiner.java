package com.change_vision.astah.extension.plugin.cplusreverse.internal;

import ch.qos.logback.core.PropertyDefinerBase;

import com.change_vision.astah.extension.plugin.cplusreverse.AstahAPIHandler;

public class AstahEditionPropertyDefiner extends PropertyDefinerBase {

	private AstahAPIHandler handler = new AstahAPIHandler();

	@Override
	public String getPropertyValue() {
		String edition = handler.getEdition();
		if (edition.isEmpty()) {
			edition = "professional";
		}
		return edition;
	}
}