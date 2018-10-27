package com.merchant;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.paypal.api.openidconnect.Session;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.CancelNotification;
import com.paypal.api.payments.ChargeModels;
import com.paypal.api.payments.Currency;
import com.paypal.api.payments.Image;
import com.paypal.api.payments.Invoice;
import com.paypal.api.payments.Invoices;
import com.paypal.api.payments.MerchantPreferences;
import com.paypal.api.payments.Notification;
import com.paypal.api.payments.Patch;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentDefinition;
import com.paypal.api.payments.Plan;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Search;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

public class SubscriptionSample {
	
    public static final String clientID = "AWxgduS3K7iwn9s81Ba3GTeTO5y2DTbO8yIZxiLkvLCQRZhcFGl7JEgVSSEXbSwA3kwGeO8Mn97AjrwJ";
    public static final String clientSecret = "EJDtq3BdQzX55c36su29zV-RiWXf1gXRWk32FeXf9aYLnQ-bZoix4rmj0i9Q05CePo_Gb_L00-e4pK9u";
    protected Plan instance = null;

	/**
	 * Create a plan.
	 *
	 * https://developer.paypal.com/webapps/developer/docs/api/#create-a-plan
	 *
	 * @return newly created Plan instance
	 * @throws PayPalRESTException
	 */
	public Plan create(APIContext context) throws PayPalRESTException, IOException {
		// Build Plan object
		Plan plan = new Plan();
		plan.setName("T-Shirt of the Month Club Plan");
		plan.setDescription("Template creation.");
		plan.setType("fixed");

		//payment_definitions
		PaymentDefinition paymentDefinition = new PaymentDefinition();
		paymentDefinition.setName("Regular Payments");
		paymentDefinition.setType("REGULAR");
		paymentDefinition.setFrequency("MONTH");
		paymentDefinition.setFrequencyInterval("1");
		paymentDefinition.setCycles("12");

		//currency
		Currency currency = new Currency();
		currency.setCurrency("USD");
		currency.setValue("20");
		paymentDefinition.setAmount(currency);

		//charge_models
		ChargeModels chargeModels = new com.paypal.api.payments.ChargeModels();
		chargeModels.setType("SHIPPING");
		chargeModels.setAmount(currency);
		List<ChargeModels> chargeModelsList = new ArrayList<ChargeModels>();
		chargeModelsList.add(chargeModels);
		paymentDefinition.setChargeModels(chargeModelsList);

		//payment_definition
		List<PaymentDefinition> paymentDefinitionList = new ArrayList<PaymentDefinition>();
		paymentDefinitionList.add(paymentDefinition);
		plan.setPaymentDefinitions(paymentDefinitionList);

		//merchant_preferences
		MerchantPreferences merchantPreferences = new MerchantPreferences();
		merchantPreferences.setSetupFee(currency);
		merchantPreferences.setCancelUrl("http://www.cancel.com");
		merchantPreferences.setReturnUrl("http://www.return.com");
		merchantPreferences.setMaxFailAttempts("0");
		merchantPreferences.setAutoBillAmount("YES");
		merchantPreferences.setInitialFailAmountAction("CONTINUE");
		plan.setMerchantPreferences(merchantPreferences);

		this.instance = plan.create(context);
		return this.instance;
	}

	/**
	 * Update a plan
	 *
	 * https://developer.paypal.com/webapps/developer/docs/api/#update-a-plan
	 *
	 * @return updated Plan instance
	 * @throws PayPalRESTException
	 */
	public Plan update(APIContext context) throws PayPalRESTException, IOException {

		List<Patch> patchRequestList = new ArrayList<Patch>();
		Map<String, String> value = new HashMap<String, String>();
		value.put("state", "ACTIVE");

		Patch patch = new Patch();
		patch.setPath("/");
		patch.setValue(value);
		patch.setOp("replace");
		patchRequestList.add(patch);

		this.instance.update(context, patchRequestList);
		return this.instance;
	}

	/**
	 * Retrieve a plan
	 *
	 * https://developer.paypal.com/webapps/developer/docs/api/#retrieve-a-plan
	 *
	 * @return the retrieved plan
	 * @throws PayPalRESTException
	 */
	public Plan retrieve(APIContext context) throws PayPalRESTException {
		return Plan.get(context, this.instance.getId());
	}

	/**
	 * Main method that calls all methods above in a flow.
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			SubscriptionSample subscriptionSample = new SubscriptionSample();

			APIContext context = new APIContext(clientID, clientSecret, "sandbox");
			
			subscriptionSample.create(context);
			System.out.println("create response:\n" + Plan.getLastResponse());
			subscriptionSample.update(context);
			System.out.println("plan updated");
			subscriptionSample.retrieve(context);
			System.out.println("retrieve response:\n" + Plan.getLastResponse());
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (PayPalRESTException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
}