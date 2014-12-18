package com.house.smart.remote.database;

import com.house.smart.remote.SmartHouseButtons;

public class ButtonValue {

	private long id;
	private String buttonName;
	private String buttonString;
	private String buttonImage;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getButtonName() {
		return buttonName;
	}
	public void setButtonName(String buttonName) {
		this.buttonName = buttonName;
	}
	public String getButtonString() {
		return buttonString;
	}
	public void setButtonString(String buttonString) {
		this.buttonString = buttonString;
	}
	public String getButtonImage() {
		return buttonImage;
	}
	public void setButtonImage(String buttonImage) {
		this.buttonImage = buttonImage;
	}

    public ButtonValue(int id, String buttonName, String buttonString) {
        this.id = id;
        this.buttonName = buttonName;
        this.buttonString = buttonString;
    }

    public ButtonValue(String buttonName, String buttonString) {
        this.buttonName = buttonName;
        this.buttonString = buttonString;
    }

    public ButtonValue() {

    }

    public ButtonValue(SmartHouseButtons smartHouseButtons) {
        this.buttonName = smartHouseButtons.getButtonName().toString();
        this.buttonString = smartHouseButtons.getButtonString().toString();
        this.id = smartHouseButtons.getId();
    }
}
