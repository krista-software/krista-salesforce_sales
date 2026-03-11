/*
 * Salesforce Sales Extension for Krista
 * Copyright (C) 2025 Krista Software
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package app.krista.extensions.crm.sales.salesforce_sales.util.xmlparser.opportunity_notification;

import app.krista.model.base.FreeForm;

import javax.xml.bind.annotation.XmlElement;

import static app.krista.extensions.crm.sales.salesforce_sales.util.Constants.ENTERPRISE_SOAP_SFORCE_COM;
import static app.krista.extensions.crm.sales.salesforce_sales.util.Constants.TEXT;

public final class SalesforceXmlOpportunity {
    @XmlElement(name = "Id", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public String Id;
    @XmlElement(name = "Amount", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public double Amount;
    @XmlElement(name = "StageName", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public String StageName;
    @XmlElement(name = "Name", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public String Name;

    public FreeForm toOpportunityFreeForm() {
        FreeForm freeForm = new FreeForm();
        freeForm.put("Id", TEXT, Id);
        freeForm.put("Amount", TEXT, Amount);
        freeForm.put("StageName", TEXT, StageName);
        freeForm.put("Name", TEXT, Name);
        return freeForm;
    }
}