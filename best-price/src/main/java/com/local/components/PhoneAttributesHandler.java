package com.local.components;

import java.util.List;

import org.springframework.stereotype.Component;

import com.model.Phone;

@Component
public class PhoneAttributesHandler {

	public String[] getDetailsOfTitlePhone(String phoneDetails, int attributesNumber) {
		String[] atrs = phoneDetails.split("\\s");
		String atr1 = atrs[0].toLowerCase().trim();
		String atr2 = atrs[1].toLowerCase().trim();
		String atr3 = atrs[2].toLowerCase().trim();
		String atrGb = "";
		int indexGb = phoneDetails.toLowerCase().lastIndexOf("gb");
		if (indexGb > 0)
			atrGb = phoneDetails.substring(phoneDetails.substring(0, indexGb).lastIndexOf(" ") + 1, indexGb).trim();
		else
			atrGb = atrs[atrs.length - 3].toLowerCase().trim();
		String lastAtr1 = atrs[atrs.length - 3].toLowerCase().trim();
		String lastAtr2 = atrs[atrs.length - 2].toLowerCase().trim();
		String lastAtr3 = atrs[atrs.length - 1].toLowerCase().trim();
		String[] attributes = new String[7];
		if (attributesNumber <= 4) {
			attributes[0] = atr1;
			attributes[1] = atr2;
			attributes[2] = atrGb;
			attributes[3] = atrGb;
			attributes[4] = lastAtr1;
			attributes[5] = lastAtr2;
			attributes[6] = lastAtr3;
		}
		if (attributesNumber > 4) {
			attributes[0] = atr1;
			attributes[1] = atr2;
			attributes[2] = atr3;
			attributes[3] = atrGb;
			attributes[4] = lastAtr1;
			attributes[5] = lastAtr2;
			attributes[6] = lastAtr3;
		}
		return attributes;
	}

	public Phone checkResultsAndGetPhoneCorespodingToDB(List<Phone> result) {
		boolean checkEqual = true;
		Phone phoneWithMinimWords = null;

		if (result.size() > 1) {

			Phone firstResult = result.get(0);
			for (Phone phone : result) {
				if (phone.getTitle().length() < firstResult.getTitle().length()) {
					checkEqual = false;
					phoneWithMinimWords = phone;
				}
			}
			if (checkEqual == true)
				return firstResult;
			else
				return phoneWithMinimWords;
		}
		if (result.size() == 1)
			return result.get(0);
		return null;
	}

}
