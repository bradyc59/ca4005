import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.SecureRandom;

public class Assignment2 {

    public static BigInteger PrimeGenerator() {
        SecureRandom randomNumber = new SecureRandom();

        BigInteger prime = BigInteger.probablePrime(512, randomNumber);

        return prime;
    }

    public static BigInteger ProductPrimes(BigInteger p, BigInteger q){
        return p.multiply(q);
    }

    public static BigInteger EulersToitent(BigInteger p, BigInteger q){
        BigInteger pMinusOne = p.subtract(BigInteger.ONE);
        BigInteger qMinusOne = q.subtract(BigInteger.ONE);

        BigInteger eulers = pMinusOne.multiply(qMinusOne);

        return eulers;

    }

    public static BigInteger gcd(BigInteger eu, BigInteger ex)
    {
        BigInteger z = BigInteger.ONE;
        if (eu == BigInteger.ZERO) {
            z = BigInteger.ZERO;
            return ex;
        }

        BigInteger GCD = gcd(ex.mod(eu), eu);

        return GCD;
    }

    public static BigInteger multiplicativeInverse(BigInteger ex, BigInteger eu){
        BigInteger gcdResult = gcd(eu, ex);
        BigInteger result = BigInteger.ZERO;
        if(gcdResult.equals(BigInteger.ONE)){
            result = gcdResult.mod(eu).add(eu).mod(eu);

        }
        return result;

    }

    private static byte[] readFile(String filename) {
        File fileIn = new File(filename);
        byte[] fileBytes = new byte[(int) fileIn.length()];

        try {

            FileInputStream fileInStream = new FileInputStream(fileIn);
            fileInStream.read(fileBytes);
            fileInStream.close();

        } catch (IOException e) {

        }

        return fileBytes;
    }

    public static BigInteger decrypt(BigInteger message, BigInteger p, BigInteger q){
        BigInteger qMultiInverseP = multiplicativeInverse(q, p);

        return chineseRemainderTheorem(message, qMultiInverseP, p, q);
    }

    public static BigInteger chineseRemainderTheorem(BigInteger message, BigInteger inverse, BigInteger p, BigInteger q){
        BigInteger primeP = modPower(message, inverse.mod(p.subtract(BigInteger.ONE)), p);
        BigInteger primeQ = modPower(message, inverse.mod(q.subtract(BigInteger.ONE)), q);

        BigInteger qInv = gcd(q,p);

        return primeQ.add(q.multiply((qInv.multiply(primeP.subtract(primeQ))).mod(p)));
    }

    public static BigInteger modPower(BigInteger base, BigInteger exponent, BigInteger modulus) {
        BigInteger result = BigInteger.ONE;
        base = base.mod(modulus);
        for (int i = 0; i < exponent.bitLength(); ++i) {
            if (exponent.testBit(i)) {
                result = result.multiply(base).mod(modulus);
            }
            base = base.multiply(base).mod(modulus);
        }
        return result;
    }

    public static void main(String[] args) {
        String filename = args[0];

        BigInteger p = PrimeGenerator();
        BigInteger q = PrimeGenerator();

        BigInteger n = ProductPrimes(p, q);

        BigInteger eulers = EulersToitent(p, q);

        boolean isRelativePrime = false;

        
        BigInteger exponent = new BigInteger("65537");
        
        while(!isRelativePrime){
            if(gcd(eulers, exponent).equals(BigInteger.ONE)){
                isRelativePrime = true;
            }
            else{
                p = PrimeGenerator();
                q = PrimeGenerator();

                n = ProductPrimes(p, q);
            }
        }

        // BigInteger multiInverseResult = multiplicativeInverse(exponent, eulers);

        MessageDigest sha256 = null;
        try {
            sha256 = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
        }

        String nToHex = n.toString(16);
        try {
            PrintWriter nOut = new PrintWriter("Modulus.txt");
            nOut.print(nToHex);
            nOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        byte[] byteMessage = sha256.digest(readFile(filename));
        BigInteger FileAsBigInteger = new BigInteger(1, byteMessage);

        BigInteger signedMessage = decrypt(FileAsBigInteger, p, q);
        String signedMessageAsString = signedMessage.toString(16);

        System.out.println(signedMessageAsString);

    }
}
