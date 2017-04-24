package kodowanie;

import java.util.ArrayList;

public class Code {

    private int n;
    private int k;

    private Polynominal generation;

    private Matrix codeWords;
    private Matrix matrixG;
    private Matrix matrixH;

    private int d; // odl. minimalna
    private int l; // zdolność detekcji
    private int t; // zdolność korekcji

    private void detectionAbility() {
        int minHam = codeWords.get(1).hammingWeight();
        int temp;
        for(int i = 2; i<codeWords.size(); i++) {
            temp = codeWords.get(i).hammingWeight();
            if(minHam > temp) minHam = temp;
        }
        d = minHam;
        l = d - 1;
        t = l/2;
        Lo.g("Zdolność korekcyjna: " + t);
    }

    public void setBody(int n, int k, String gen_field) {
        if(codeWords != null) codeWords.clear();
        if(matrixG != null) matrixG.clear();
        if(matrixH != null) matrixH.clear();
        if(generation != null) generation = null;
        this.n = n;
        this.k = k;

        if(gen_field.length() != n-k+1) {
            Lo.g("Wielomian generacyjny ma mieć N-K+1!!!!!!");
        } else {
            generation = new Polynominal(gen_field);
            Lo.g("Ciało utworzone. n=" + n + ". k=" + k);
        }

        Lo.g("Czy kod jest cykliczny: "+isCodeCyclic());

    }

    public void createCodeWords() {
        codeWords = new Matrix(n);

        for(Polynominal p : new Matrix(k,false)) {
            p = p.multiByMatrix(matrixG);
            codeWords.add(p);
        }

        Lo.g("Słowa kodowe: --------");
        codeWords.present();
        Lo.g("Słowa kodowe utworzone.");

        detectionAbility();
    }

    public boolean isCodeCyclic() {
        Polynominal xnPlusOne = new Polynominal("1");
        for(int i=0; i < n-1; i++) {
            xnPlusOne.appendAtBack(0);
        }
        xnPlusOne.appendAtBack(1);
        while(true) {
            xnPlusOne = xnPlusOne.add(generation);
            try {
                while (xnPlusOne.toString().charAt(0) == '0') xnPlusOne.deleteCharAt(0);
            }catch (StringIndexOutOfBoundsException e) {
                return true;
            }
            if(xnPlusOne.getLength() < generation.getLength()) break;
        }
        if(xnPlusOne.toString().matches("(.*)1(.*)")) return false;
        else return true;
    }

    private Matrix getReturnsOfDivNeededToGMatrix(Polynominal row1, Polynominal gen) {
        ArrayList<Polynominal> reverseResults = new ArrayList<>();
        boolean nextAddZero = false;
        Polynominal result = new Polynominal(row1.toString());
        Polynominal polyZero = new Polynominal();
        do {
            if (nextAddZero) {
                for (int i = 0; i < result.getLength(); i++) {
                    polyZero.appendAtBack(0);
                }
                result = result.add(polyZero);
                polyZero = new Polynominal();
            } else result = result.add(gen);
            result.deleteCharAt(0);
            Polynominal wordPL = new Polynominal();
            for (int i = 0; i < gen.getLength() - 1; i++) wordPL.appendAtBack(result.charAt(i));
            reverseResults.add(wordPL);
            nextAddZero = result.charAt(0) == 0;
        } while(result.getLength() > gen.getLength()-1);
        Matrix myMatrix = new Matrix(gen.getLength()-1);
        for(int i=reverseResults.size()-1; i>=0; i--) {
            myMatrix.add(reverseResults.get(i));
        }
        return myMatrix;
    }

    public void createMatrixG()    {
        matrixG = new Matrix(n);

        Polynominal row1 = new Polynominal("1");
            for(int i=0; i<(n-1); i++) {
            row1.appendAtBack(0);
        }

        Matrix matrixOfGeneratedDiv = getReturnsOfDivNeededToGMatrix(row1, generation);

        for(int i=0; i<k; i++) {
            matrixG.add(new Polynominal(new Matrix(k,true).get(i) + "" + matrixOfGeneratedDiv.get(i)));
        }
        matrixG.present();
        Lo.g("Macierz G utworzona.");
    }

    public void createMatrixH() {
        matrixH = new Matrix(n-k);
        for(int j = 0; j < k; j++) {
            Polynominal pl = new Polynominal();
            for (int i = k; i < n; i++) {
                pl.appendAtBack(matrixG.get(j).charAt(i));
            }
            matrixH.add(pl);
        }
        for( Polynominal p : new Matrix(n-k,true)) {
            matrixH.add(p);
        }
        Lo.g("Macierz H utworzona.");
        matrixH.present();
    }

    public String checkCodeWord(Polynominal word) {
        String result = "";
        Lo.g("Słowo kodowe: "+word);
        if(word.getLength() != n) return "Zła długość.\n";
        Polynominal syndrome = word.multiByMatrix(matrixH);
        for(int i=0; i<k; i++) syndrome.appendAtStart(0);
        int hammingWeight = syndrome.hammingWeight();
        Lo.g("Syndrom: "+syndrome + " Hamming: "+ hammingWeight);
        int slideCounter = 0;
        boolean corrected = false;
        for(int i=0; i<k+3; i++) {
            if(hammingWeight==0) {
                corrected = true;
                Lo.g("Słowo poprawne!");
                result = "Słowo poprawne.\n";
                break;
            } else if(hammingWeight<=t) {
                word = word.add(syndrome);
                for(int j=0; j<slideCounter; j++) {
                    word = word.slideRight();
                }
                corrected = true;
                Lo.g("Skorygowano błąd.\nSkorygowane słowo: "+word+"\n");
                result = "Skorygowano błąd.\nSkorygowane słowo: "+word+"\n";
                break;
            } else {
                word = word.slideLeft();
                slideCounter++;
                Lo.g("Przesuwam poraz " + slideCounter);
                syndrome = word.multiByMatrix(matrixH);
                for(int j=0; j<k; j++) syndrome.appendAtStart(0);
                hammingWeight = syndrome.hammingWeight();
                Lo.g("Syndrom: "+syndrome + " Waga Hamminga: "+ hammingWeight);
            }

        }
        if(!corrected) {
            Lo.g("Nie skorygowano błędu.\n");
            result = "Nie można skorygować słowa kodowego.\n";
        }
        return result;
    }

    public String present() {
        return  "\nParametry kodu:\n" +
                "- odl. min. d = "+d+"\n" +
                "- zd. detekcji l = "+l+"\n"+
                "- zd. korekcji t = "+t+"\n" +
                "----------------\n";
    }


    public Matrix getCodeWords() {
        return codeWords;
    }

    public Matrix getMatrixG() {
        return matrixG;
    }

    public Matrix getMatrixH() {
        return matrixH;
    }
}
