package kodowanie;

import java.util.ArrayList;

public class Matrix extends ArrayList<Polynominal> {
    private final int width;

    public Matrix(int x) {
        super();
        width = x;
    }

    public Matrix(int x, boolean one) {
        super();
        width = x;
        if(one) createOneMatrix();
        else createBitMatrix();
    }

    @Override
    public boolean add(Polynominal polynominal) {
        if(polynominal.getLength() != width) {
            Lo.g("Zła długość wielomianu. Wielomian nie zostanie dodany");
            return false;
        } else return super.add(polynominal);
    }

    public int getWidth() {
        return width;
    }

    public String present() {
        StringBuilder sb = new StringBuilder();
        Lo.g("Macierz=");
        for(Polynominal p : this) {
            Lo.g("| "+p+" |");
            String x = "| "+p+" |\n";
            sb.append(x);
        }
        Lo.g("-----------");
        sb.append("-----------\n");
        return sb.toString();
    }

    private void createOneMatrix() {
        for(int i=0; i<width; i++) {
            Polynominal pl = new Polynominal();
            for(int j=0; j<width; j++) {
                pl.appendAtBack(0);
            }
            pl.setCharAt(i,1);
            add(pl);
        }
    }

    private void createBitMatrix() {
        int myK = (int) Math.pow(2, width);
        for(int i=0; i<myK; i++) {
            Polynominal pl = new Polynominal();
            int j = i;
            while (j != 0) {
                pl.appendAtBack(j % 2);
                j /= 2;
            }
            while(pl.getLength()<width) pl.appendAtBack(0);
            pl.reverse();
            add(pl);
        }
    }
}
