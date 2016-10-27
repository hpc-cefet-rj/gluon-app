package com.gluonhq.beacons.settings;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Settings {

	public static final String UUID = "74278BDA-B644-4520-8F0C-720EAF059935";
	
	private final StringProperty uuid = new SimpleStringProperty(UUID);

	public final StringProperty uuidProperty() {
		return this.uuid;
	}
	
	public final String getUuid() {
		return this.uuidProperty().get();
	}

	public final void setUuid(final String uuid) {
		this.uuidProperty().set(uuid);
	}
	
}
