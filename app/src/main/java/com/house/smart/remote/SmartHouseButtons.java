package com.house.smart.remote;


public enum SmartHouseButtons {
	BUTTON1("BUTTON1",SmartHouseButtonsType.SENDING_BUTTON),
	BUTTON2("BUTTON2",SmartHouseButtonsType.SENDING_BUTTON),
	BUTTON3("BUTTON3",SmartHouseButtonsType.SENDING_BUTTON),
	BUTTON4("BUTTON4",SmartHouseButtonsType.SENDING_BUTTON),
	BUTTON5("BUTTON5",SmartHouseButtonsType.SENDING_BUTTON),
	BUTTON6("BUTTON6",SmartHouseButtonsType.SENDING_BUTTON),
	BUTTON7("BUTTON7",SmartHouseButtonsType.SENDING_BUTTON),
	BUTTON8("BUTTON8",SmartHouseButtonsType.SENDING_BUTTON),
	BUTTON9("BUTTON9",SmartHouseButtonsType.SENDING_BUTTON),
	BUTTON10("BUTTON10",SmartHouseButtonsType.SENDING_BUTTON),
	BUTTON11("BUTTON11",SmartHouseButtonsType.SENDING_BUTTON),
	BUTTON12("BUTTON12",SmartHouseButtonsType.SENDING_BUTTON);

	 CharSequence mText; // Display Text
	 SmartHouseButtonsType mCategory;
		
	 SmartHouseButtons(CharSequence text,SmartHouseButtonsType category) {
	    mText = text;
	    mCategory = category;
	  }

	  public CharSequence getText() {
	    return mText;
	  }
}
