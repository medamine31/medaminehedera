package org.example;

import com.hedera.hashgraph.sdk.*;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.concurrent.TimeoutException;

public class Main {
    public static void main(String[] args) throws PrecheckStatusException, TimeoutException, ReceiptStatusException {
        AccountId myAccountId = AccountId.fromString(Dotenv.load().get("MY_ACCOUNT_ID"));
        PrivateKey myPrivateKey = PrivateKey.fromString(Dotenv.load().get("MY_PRIVATE_KEY"));
        AccountId AdminId = AccountId.fromString(Dotenv.load().get("admineid"));
        PrivateKey AdminKey = PrivateKey.fromString(Dotenv.load().get("adminkey"));
        Client client = Client.forTestnet();
        client.setOperator(myAccountId, myPrivateKey);
        Client admin = Client.forTestnet();
        admin.setOperator(AdminId, AdminKey);
        //Create the transaction
        TopicCreateTransaction transaction = new TopicCreateTransaction();
        transaction.setTopicMemo("bonjour je suis mohamed amine");

//Sign with the client operator private key and submit the transaction to a Hedera network
        TransactionResponse txResponse = transaction.execute(client);

//Request the receipt of the transaction
        TransactionReceipt receipt = txResponse.getReceipt(client);

//Get the topic ID
        TopicId newTopicId = receipt.topicId;
       String topic = transaction.getTopicMemo();

        System.out.println("The new topic ID is " + newTopicId);
        System.out.println("The topic memo is " + topic);
//
 //update the topic
        TopicUpdateTransaction transaction1= new TopicUpdateTransaction();
        transaction1.setTopicId(receipt.topicId);
        transaction1.setTopicMemo("hello world");

//Sign the transaction with the admin key to authorize the update
        TopicUpdateTransaction signTx = transaction1.freezeWith(client).sign(AdminKey);
        //Sign the transaction with the client operator, submit to a Hedera network, get the transaction ID
        TransactionResponse trxResponse1 = signTx.execute(client);
        //Request the receipt of the transaction
        TransactionReceipt receipt1 = trxResponse1.getReceipt(client);

//Get the transaction consensus status
        Status transactionStatus1 = receipt1.status;

        System.out.println("The transaction consensus status is " +transactionStatus1);
        System.out.println("the new memo : " + transaction1.getTopicMemo());
        //Create the transaction
        TopicMessageSubmitTransaction transaction3 = new TopicMessageSubmitTransaction()
                .setTopicId(receipt.topicId)
                .setMessage("hello, this is my message ");

//Sign with the client operator key and submit transaction to a Hedera network, get transaction ID
        TransactionResponse txResponse3 = transaction3.execute(client);

//Request the receipt of the transaction
        TransactionReceipt receipt3 = txResponse3.getReceipt(client);

//Get the transaction consensus status
        Status transactionStatus3 = receipt3.status;

        System.out.println("The transaction consensus status is " +transactionStatus3);
  System.out.println("the message : "+ transaction3.getMessage());
    }
}