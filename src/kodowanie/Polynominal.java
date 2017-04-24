package kodowanie;

public class Polynominal {
    private StringBuilder poly;

    public Polynominal(String name) {
        poly = new StringBuilder(name);
    }

    public Polynominal() {
        poly = new StringBuilder();
    }

    public Polynominal add(Polynominal b) {
        Polynominal polynominal = new Polynominal();
        int len;
        if (this.poly.length() > b.poly.length()) len = this.poly.length();
        else len = b.poly.length();

        for (int i = 0; i < len; i++) {
            int arg1, arg2;

            try {
                arg1 = this.charAt(i);
            } catch (Exception e) {
                arg1 = 0;
            }
            try {
                arg2 = b.charAt(i);
            } catch (Exception e) {
                arg2 = 0;
            }

            int number = arg1 + arg2;
            int result = number % 2;
            polynominal.appendAtBack(result);
        }
        //Lo.g("Wynik dodawania: "+this + " + "+ b + " = " + polynominal);
        return polynominal;

    }

    public void appendAtBack(int app) {
        this.poly.append(app);
    }

    public void appendAtStart(int app) {
        this.reverse();
        this.appendAtBack(app);
        this.reverse();
    }

    public Polynominal multiByMatrix(Matrix m) {
        Polynominal p = new Polynominal();
        for (int i = 0; i < m.getWidth(); i++) p.appendAtBack(0);
        for (int i = 0; i < m.size(); i++) {
            if (this.charAt(i) == 1) {
                p = p.add(m.get(i));
            }
        }
        return p;
    }

    public Polynominal slideLeft() {
        Polynominal pl = new Polynominal();
        for (int i = 0; i < this.poly.length() - 1; i++) {
            pl.appendAtBack(this.charAt(i + 1));
        }
        pl.appendAtBack(this.charAt(0));
        return pl;
    }

    public Polynominal slideRight() {
        Polynominal pl = new Polynominal();
        pl.appendAtBack(charAt(poly.length() - 1));
        for (int i = 1; i < this.poly.length(); i++) {
            pl.appendAtBack(charAt(i - 1));
        }
        return pl;
    }

    public int hammingWeight() {
        int hw = 0;
        for (int i = 0; i < this.poly.length(); i++) {
            if (charAt(i) == 1) hw++;
        }
        return hw;
    }

    public int getLength() {
        return poly.length();
    }

    public int charAt(int index) {
        return Integer.parseInt(poly.charAt(index) + "");
    }

    public void deleteCharAt(int index) {
        poly.deleteCharAt(index);
    }

    @Override
    public String toString() {
        return poly.toString();
    }

    public void setCharAt(int i, int i1) {
        char c;
        if (i1 == 1) c = '1';
        else c = '0';
        poly.setCharAt(i, c);
    }

    public Polynominal reverse() {
        return new Polynominal(poly.reverse().toString());
    }
}
