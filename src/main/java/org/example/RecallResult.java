package org.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RecallResult {
    public String status;
    public String city;
    public String state;
    public String country;
    public String classification;
    public String product_type;
    public String event_id;
    public String recalling_firm;
    public String address_1;
    public String address_2;
    public String postal_code;
    public String voluntary_mandated;
    public String initial_firm_notification;
    public String distribution_pattern;
    public String recall_number;
    public String product_description;
    public String product_quantity;
    public String reason_for_recall;
    public String recall_initiation_date;
    public String center_classification_date;
    public String report_date;
    public String code_info;

    public String getCode_info() {
        return code_info;
    }

    @Override
    public String toString() {
        return "{\n" +
                 "\"recall_number\" : \""   + recall_number + '\"'+",\n" +
                "\"status\" : \"" + status + '\"' +",\n"+
                "\"city\" : \"" + city + '\"'+",\n" +
                "\"state\" : \"" + state + '\"'+",\n" +
                "\"country\" : \"" + country + '\"' +",\n"+
                "\"classification\" : \"" + classification + '\"' +",\n"+
                "\"product_type\" : \"" + product_type + '\"' +",\n"+
                "\"recalling_firm\" : \"" + recalling_firm + '\"' +",\n"+
                "\"address_1\" : \"" + address_1 + '\"'+",\n" +
                "\"address_2\" : \"" + address_2 + '\"' +",\n"+
                "\"postal_code\" : \"" + postal_code + '\"' +",\n"+
                "\"voluntary_mandated\" : \"" + voluntary_mandated + '\"' +",\n"+
                "\"initial_firm_notification\" : \"" + initial_firm_notification + '\"' +",\n"+
                "\"distribution_pattern\" : \"" + distribution_pattern + '\"'+",\n" +
                "\"product_description\" : \"" + product_description + '\"' +",\n"+
                "\"product_quantity\" : \"" + product_quantity + '\"' +",\n"+
                "\"reason_for_recall\" : \"" + reason_for_recall + '\"' +",\n"+
                "\"recall_initiation_date\" : \"" + recall_initiation_date + '\"' +",\n"+
                "\"center_classification_date\" : \"" + center_classification_date + '\"' +",\n"+
                "\"report_date\" : \"" + report_date + '\"' +",\n"+
                "\"code_info\" : \"" + code_info + '\"' +"\n"+
                '}';
    }
}

