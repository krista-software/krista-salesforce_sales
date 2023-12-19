package app.krista.extensions.crm.sales.salesforce_sales.catalog;

import app.krista.extension.impl.anno.CatalogRequest;
import app.krista.extension.impl.anno.Domain;
import app.krista.extension.impl.anno.Field;
import app.krista.extensions.crm.sales.salesforce_sales.impl.SalesforceManager;
import app.krista.extensions.crm.sales.salesforce_sales.util.Validators;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.text.DecimalFormat;
import java.util.Random;

@Domain(id = "catEntryDomain_086f33da-aaea-4d0c-816f-3d5f20b4a273",
        name = "Sales",
        ecosystemId = "catEntryEcosystem_8a0e8ce7-6289-492f-9202-52284024280c",
        ecosystemName = "CRM",
        ecosystemVersion = "db320c64-6f6f-4c9e-8daa-41d3f796d6a6")
public final class ActionsArea {

    private final SalesforceManager salesforceManager;
    private static final Logger LOGGER = LoggerFactory.getLogger(ActionsArea.class);


    @Inject
    public ActionsArea(SalesforceManager salesforceManager) {
        this.salesforceManager = salesforceManager;
    }

    @CatalogRequest(
            id = "localDomainRequest_2ebadd34-8bcd-46b9-a62b-b3972181ead9",
            name = "Send A Mail",
            description = "This request will send an email with the given subject, and body to the comma-separated email addresses",
            area = "Actions",
            type = CatalogRequest.Type.CHANGE_SYSTEM)
    @Field(name = "Status", type = "Switch", required = false)
    public Boolean sendAMail(
            @Field(name = "Email Addresses", type = "Text") String emailAddresses,
            @Field(name = "Subject", type = "Text") String subject,
            @Field(name = "Body", type = "Paragraph") String body) {
        Validators.validation(emailAddresses, subject, body);
        try {
            salesforceManager.sendEmail(emailAddresses, subject, body);
            return true;
        } catch (IllegalArgumentException cause) {
            LOGGER.error(cause.getMessage(), cause);
            return false;
        }
    }

    @CatalogRequest(
            id = "localDomainRequest_18265c9a-fca0-473d-9c20-302c3896b893",
            name = "Send Verification Mail",
            description = "This request will send a verification email with the given subject, and body to the comma-separated email addresses",
            area = "Actions",
            type = CatalogRequest.Type.CHANGE_SYSTEM)
    @Field(name = "Verification Code", type = "Text", required = false)
    public String sendVerificationMail(
            @Field(name = "Email Addresses", type = "Text") String emailAddresses,
            @Field(name = "Subject", type = "Text") String subject,
            @Field(name = "Body", type = "Paragraph") String body) {
        Validators.validation(emailAddresses, subject, body);
        try {
            String otp = new DecimalFormat("000000").format(new Random().nextInt(999999));
            boolean isOtpAdded = false;
            if (body.contains("$code")) {
                isOtpAdded = true;
                body = body.replace("$code", otp);
            }
            if (body.contains("$otp")) {
                isOtpAdded = true;
                body = body.replace("$otp", otp);
            }
            if (!isOtpAdded) {
                body = body + "\n Verification Code: " + otp;
            }

            salesforceManager.sendEmail(emailAddresses, subject, body);
            return otp;
        } catch (IllegalArgumentException cause) {
            LOGGER.error(cause.getMessage(), cause);
            return "Failed to send verification mail";
        }
    }

}
