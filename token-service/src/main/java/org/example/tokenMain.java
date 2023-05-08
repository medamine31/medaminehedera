package org.example;

import com.hedera.hashgraph.sdk.*;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.concurrent.TimeoutException;

public class tokenMain {
    public static void main(String[] args) throws PrecheckStatusException, TimeoutException, ReceiptStatusException {
        AccountId myAccountId = AccountId.fromString(Dotenv.load().get("MY_ACCOUNT_ID"));
        PrivateKey myPrivateKey = PrivateKey.fromString(Dotenv.load().get("MY_PRIVATE_KEY"));
        Client client = Client.forTestnet();
        client.setOperator(myAccountId, myPrivateKey);
       AccountId AdminId = AccountId.fromString(Dotenv.load().get("ADMIN_ID"));
      PrivateKey AdminKey = PrivateKey.fromString(Dotenv.load().get("ADMIN_KEY"));
//        Client admin = Client.forTestnet();
//        admin.setOperator(AdminId, AdminKey);
        //Create the transaction
        // 1 token is sent to this account everytime it is transferred

//Version: 2.0.14
        TokenCreateTransaction transaction = new TokenCreateTransaction()

                .setTokenName("nft")
                .setTokenSymbol("F")
                .setTokenType(TokenType.NON_FUNGIBLE_UNIQUE)
                .setTreasuryAccountId(myAccountId)
                .setAdminKey(AdminKey)
                .setInitialSupply(0)
                .setSupplyKey(myPrivateKey)
                .setAdminKey(myPrivateKey.getPublicKey())
                .freezeWith(client);//Change the default max transaction fee

//Build the unsigned transaction, sign with admin private key of the token, sign with the token treasury private key, submit the transaction to a Hedera network
        TransactionResponse txResponse = transaction.freezeWith(client).sign(myPrivateKey).sign(myPrivateKey).execute(client);

//Request the receipt of the transaction
        TransactionReceipt receipt = txResponse.getReceipt(client);

//Get the token ID from the receipt
        TokenId tokenId = receipt.tokenId;

        System.out.println("The new token ID is " + tokenId);

//v2.0.1
        //Create the transaction
        TokenUpdateTransaction transactionnft = new TokenUpdateTransaction()
                .setTokenId(tokenId)
                .setTokenName("med amine nft");

//Freeze the unsigned transaction, sign with the admin private key of the token, submit the transaction to a Hedera network
        TransactionResponse txResponsenft = transaction.freezeWith(client).sign(AdminKey).execute(client);

//Request the receipt of the transaction
        TransactionReceipt receiptnft = txResponsenft.getReceipt(client);

//Get the transaction consensus status
        Status transactionStatusnft = receiptnft.status;

        System.out.println("The transaction consensus status is " +transactionStatusnft);
        //token information
        System.out.println(transactionnft.getTokenName());


        //Mint another 1,000 tokens
        TokenMintTransaction transaction5 = new TokenMintTransaction()
                .setTokenId(tokenId)
                .setMaxTransactionFee(new Hbar(5)) //Use when HBAR is under 10 cents
                .setAmount(1000);

//Freeze the unsigned transaction, sign with the supply private key of the token, submit the transaction to a Hedera network
        TransactionResponse txResponse5 = transaction5
                .freezeWith(client)
                .sign(myPrivateKey)
                .execute(client);

//Request the receipt of the transaction
        TransactionReceipt receipt5 = txResponse5.getReceipt(client);

//Obtain the transaction consensus status
        Status transactionStatus5 = receipt5.status;

        System.out.println("The transaction consensus status is " +transactionStatus5);

//v2.0.1
//v2.0.1
        //Create the transfer transaction
        //;
        TransferTransaction transaction6 = new TransferTransaction()
                .addTokenTransfer(tokenId, myAccountId, -100)
                .addTokenTransfer(tokenId, AdminId, 100);

//Sign with the client operator key and submit the transaction to a Hedera network
        TransactionResponse txResponse6 = transaction6.execute(client);

//Request the receipt of the transaction
        TransactionReceipt receipt6 = txResponse6.getReceipt(client);

//Get the transaction consensus status
        Status transactionStatus6 = receipt6.status;

        System.out.println("The transaction consensus status is " +transactionStatus6);

/// account balance
        //Create the query
        AccountBalanceQuery query = new AccountBalanceQuery()
                .setAccountId(myAccountId);

//Sign with the operator private key and submit to a Hedera network
        AccountBalance tokenBalance = query.execute(client);

        System.out.println("The token balance(s) for this account: " +tokenBalance.tokens);


        //Create the query
        AccountBalanceQuery query2 = new AccountBalanceQuery()
                .setAccountId(AdminId);

//Sign with the operator private key and submit to a Hedera network
        AccountBalance tokenBalance2= query2.execute(client);

        System.out.println("The token balance(s) for the second account: " +tokenBalance2.tokens);

//v2.0.9
        System.out.println("medx");
    }
}
