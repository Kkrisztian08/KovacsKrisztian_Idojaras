package com.company;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Ugyfelkiszolgalo implements Runnable {
    private HashMap<String, Idojaras> elorejelzesek;
    Socket kapcsolat;

    public Ugyfelkiszolgalo(Socket kapcsolat) {
        this.elorejelzesek = new HashMap<>();
        this.kapcsolat = kapcsolat;
        Beolvas();
    }

    @Override
    public void run() {
        try {
            DataInputStream ugyfeltol = new DataInputStream(kapcsolat.getInputStream());
            DataOutputStream ugyfelnek = new DataOutputStream(kapcsolat.getOutputStream());
            while (true) {
                String megye = ugyfeltol.readUTF();
                int menu;
                int mikor;
                do {
                    menu = ugyfeltol.readInt();
                    mikor = ugyfeltol.readInt();
                    if (menu == 1 && mikor == 1) {
                        ugyfelnek.writeUTF(maxMai(megye));
                    } else if (menu == 1 && mikor == 2) {
                        ugyfelnek.writeUTF(maxHolnapi(megye));
                    }  else if (menu == 2 && mikor == 1) {
                        ugyfelnek.writeUTF(minMai(megye));
                    }  else if (menu == 2 && mikor == 2) {
                        ugyfelnek.writeUTF(minHolnapi(megye));
                    }  else if (menu == 3 && mikor == 1) {
                        ugyfelnek.writeUTF(idojarasMai(megye));
                    }  else if (menu == 3 && mikor == 2) {
                        ugyfelnek.writeUTF(idojarasHolnapi(megye));
                    }
                } while (menu != 4);
            }
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    private String idojarasMai(String megye) {
        Idojaras idojaras = elorejelzesek.get(megye);
        return String.format("Mai előrejelzés %s megyében: %s", idojaras.getMegye(), idojaras.getMai().getSzovegesElorejelzes());
    }

    private String idojarasHolnapi(String megye) {
        Idojaras idojaras = elorejelzesek.get(megye);
        return String.format("Holnapi előrejelzés %s megyében: %s", idojaras.getMegye(), idojaras.getHolnapi().getSzovegesElorejelzes());
    }

    private String minMai(String megye) {
        Idojaras idojaras = elorejelzesek.get(megye);
        return String.format("Mai minimum %s megyében: %d", idojaras.getMegye(), idojaras.getMai().getMin());
    }

    private String minHolnapi(String megye) {
        Idojaras idojaras = elorejelzesek.get(megye);
        return String.format("Holnapi minimum %s megyében: %d", idojaras.getMegye(), idojaras.getHolnapi().getMin());
    }

    private String maxMai(String megye) {
        Idojaras idojaras = elorejelzesek.get(megye);
        return String.format("Mai maximum %s megyében: %d", idojaras.getMegye(), idojaras.getMai().getMax());
    }

    private String maxHolnapi(String megye) {
        Idojaras idojaras = elorejelzesek.get(megye);
        return String.format("Holnapi minimum %s megyében: %d", idojaras.getMegye(), idojaras.getHolnapi().getMax());
    }

    public void Beolvas() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("weather.txt"));
            reader.readLine();

            String sor = reader.readLine();
            while (sor != null) {
                Idojaras i = new Idojaras(sor);
                String megye = i.getMegye();
                elorejelzesek.put(megye, i);
                sor = reader.readLine();
            }

            for (Map.Entry<String, Idojaras> entry : elorejelzesek.entrySet()) {
                System.out.println(entry.getValue());
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
