package com.keysender.rsaencryption;

import java.math.BigInteger;
import java.util.Random;

public class RSAEncryption {

    private char[] alphabet = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x',
            'y','z','0','1','2','3','4','5','6','7','8','9','а', 'б', 'в', 'г', 'д', 'е', 'ж', 'з', 'и', 'й', 'к', 'л',
            'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю','я','.',',',};
    private int alphabetLen =alphabet.length;


    private Random rnd = new Random();
    public BigInteger p; // p prime number
    private BigInteger q; // q prime number

    public BigInteger publicKey; // e public key
    public BigInteger modulus; // n modulus

    private BigInteger fi; // Euler's totient function
    public BigInteger secretKey; // d secret key

    // parameters for using Chinese Remainder Theorem
    public BigInteger pSecretKey; // secret key by p prime number.
                        // pSecretKey is the same as qSecretKey

    //public BigInteger qSecretKey; // secret key by q prime number.
    // qSecretKey may not be used because result with using pSecretKey will be the same


    public RSAEncryption() {
        p = BigInteger.probablePrime(1024, rnd);
        q = BigInteger.probablePrime(1024, rnd);
        publicKey = BigInteger.probablePrime(128, rnd); // getting e public key
        modulus = p.multiply(q); // getting n modulus of p and q;
        fi = (p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE))); //getting Euler's totient function
        secretKey = (publicKey).modInverse(fi); // getting d secret key;

        pSecretKey = (secretKey).mod(p.subtract(BigInteger.ONE));
        //qSecretKey = (secretKey).mod(q.subtract(BigInteger.ONE));

    }
/*
    public BigInteger[] encrypt( BigInteger recipientPublicKey, BigInteger recipientModulus, String message ){

        char[] messageChar = message.toCharArray();
        int messageLen = messageChar.length;
        BigInteger[] messageForEncrypt = new BigInteger[messageLen];
        // should try to use only one array to write numerical sequence to it, at first,
        // and then, rewrite result of encryption there
        BigInteger[] cipher = new BigInteger[messageLen];
        String encryptedMessage="";

        for(int i = 0; i < messageLen; i++) {
            for (int j = 0; j < alphabetLen; j++) {
                if(messageChar[i] == alphabet[j]) {
                    messageForEncrypt[i] = BigInteger.valueOf(j + 1);
                }
            }
        }
        for(int i = 0; i < messageLen; i++) {
            cipher[i] = messageForEncrypt[i].modPow(recipientPublicKey, recipientModulus);
            encryptedMessage += cipher[i]+" ";
        }

        return cipher; //should try to return array of BigInteger result, instead of String type : Done!
    }
*/

    public String encrypt( BigInteger recipientPublicKey, BigInteger recipientModulus, String message ){

        char[] messageChar = message.toCharArray();
        int messageLen = messageChar.length;
        BigInteger[] messageForEncrypt = new BigInteger[messageLen];
        // should try to use only one array to write numerical sequence to it, at first,
        // and then, rewrite result of encryption there
        BigInteger[] cipher = new BigInteger[messageLen];
        String encryptedMessage="";

        for(int i = 0; i < messageLen; i++) {
            for (int j = 0; j < alphabetLen; j++) {
                if(messageChar[i] == alphabet[j]) {
                    messageForEncrypt[i] = BigInteger.valueOf(j + 1);
                }
            }
        }
        for(int i = 0; i < messageLen; i++) {
            cipher[i] = messageForEncrypt[i].modPow(recipientPublicKey, recipientModulus);
            encryptedMessage += cipher[i]+" ";
        }

        return encryptedMessage; //should try to return array of BigInteger result, instead of String type
    }

/*
    public String decrypt( BigInteger ownPSecretKey, BigInteger pPrimeNumber, BigInteger[] cipher ){

        //char[] cipherChar = cipher.toCharArray();
        //int cipherLen = cipherChar.length;
        int cipherLen = cipher.length;
       // BigInteger[] cipherForDecrypt = new BigInteger[cipherLen];
        BigInteger[] setOfDecryptedNumbers = new BigInteger[cipherLen];
        int intDecryptedNum[] = new int[cipherLen];
        String decryptedMessage ="";

        for(int i = 0; i < cipherLen; i++) {

            // one is subtracted from the result because in line *messageForEncrypt[i] = BigInteger.valueOf(j + 1)* one
            // is added to avoid zero value of index

            // the usual way to decrypt
            // setOfDecryptedNumbers[i] = (cipherForDecrypt[i].modPow(ownSecretKey,modulus).subtract(BigInteger.valueOf(1)));

            //the way using Chinese Remainder Theorem to decrypt
            setOfDecryptedNumbers[i] = (cipher[i].modPow(ownPSecretKey, pPrimeNumber).subtract(BigInteger.valueOf(1)));
            //setOfDecryptedNumbers[i] = (cipherForDecrypt[i].modPow(qSecretKey,q).subtract(BigInteger.valueOf(1)));
        }

        for(int i = 0; i < cipherLen; i++) {
            intDecryptedNum[i] = setOfDecryptedNumbers[i].intValue();
            for (int j = 0; j < alphabetLen; j++) {
                if(intDecryptedNum[i] == j) {
                    decryptedMessage += alphabet[j];
                }
            }
        }

        return decryptedMessage;
    }
*/

    public String decrypt( BigInteger ownPSecretKey, BigInteger pPrimeNumber, String cipher ){

        char[] cipherChar = cipher.toCharArray();
        int cipherLen = cipherChar.length;
        BigInteger[] cipherForDecrypt = new BigInteger[cipherLen];
        BigInteger[] setOfDecryptedNumbers = new BigInteger[cipherLen];
        int intDecryptedNum[] = new int[cipherLen];
        String decryptedMessage ="";

        for(int i = 0; i < cipherLen; i++) {
              // one is subtracted from the result because in line *messageForEncrypt[i] = BigInteger.valueOf(j + 1)* one
            // is added to avoid zero value of index

                    // the usual way to decrypt
           // setOfDecryptedNumbers[i] = (cipherForDecrypt[i].modPow(ownSecretKey,modulus).subtract(BigInteger.valueOf(1)));

            //the way using Chinese Remainder Theorem to decrypt
            setOfDecryptedNumbers[i] = (cipherForDecrypt[i].modPow(ownPSecretKey, pPrimeNumber).subtract(BigInteger.valueOf(1)));
            //setOfDecryptedNumbers[i] = (cipherForDecrypt[i].modPow(qSecretKey,q).subtract(BigInteger.valueOf(1)));
        }

        for(int i = 0; i < cipherLen; i++) {
            intDecryptedNum[i] = setOfDecryptedNumbers[i].intValue();
            for (int j = 0; j < alphabetLen; j++) {
                if(intDecryptedNum[i] == j) {
                    decryptedMessage += alphabet[j];
                }
            }
        }

        return decryptedMessage;
    }



}
