package com.ShopIT.Service.Service;//package com.ShopIT.Service;
//
//import com.twilio.exception.ApiException;
//import org.springframework.stereotype.Service;
//import com.twilio.Twilio;
//import com.twilio.rest.api.v2010.account.Message;
//import com.twilio.rest.api.v2010.account.usage.Record;
//import com.twilio.twiml.TwiMLException;
//import com.twilio.type.PhoneNumber;
//@Service
//public class TwilioService {
//    public static final String ACCOUNT_SID = System.getenv("TWILIO_ACCOUNT_SID");
//    public static final String AUTH_TOKEN = System.getenv("TWILIO_AUTH_TOKEN");
//
//    public static final PhoneNumber PHONE_NUMBER = new PhoneNumber("+18885551234");
//    public static void main(String[] args) throws TwiMLException{
//
//        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
//
//        Iterable<Record> usage = Record.reader().read();
//        for (Record record : usage) {
//            System.out.println(record);
//        }
//
//        // Get a number
////        IncomingPhoneNumber number = buyNumber();
////        System.out.println(number.getPhoneNumber());
//
//        // Send a text message
//        try {
//            Message message = Message.creator(
//                    ACCOUNT_SID,
//                    PHONE_NUMBER,
//                    new PhoneNumber("+15559994321"),  // From number
////                    number.getPhoneNumber(),
//                    "Hello world!"
//            ).create();
//        } catch (final ApiException e) {
//            System.err.println(e);
//        }
////            Message message = Message.creator(
////                    new PhoneNumber("+15558881234"),  // To number
////                    "Hello world!"                    // SMS body
////            ).create();
//
//            System.out.println(message.getSid());
//
//        System.out.println(message.getSid());
//        System.out.println(message.getBody());
//
//        // Print all the messages
//        Iterable<Message> messages = Message.reader().read();
//        for (Message m : messages) {
//            System.out.println(m.getSid());
//            System.out.println(m.getBody());
//        }
//}
