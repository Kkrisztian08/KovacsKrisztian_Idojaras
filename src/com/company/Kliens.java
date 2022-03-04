package com.company;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Kliens {
    public static void main(String[] args) {
        try {
            Socket kapcsolat = new Socket("localhost", 8080);
            DataInputStream szervertol = new DataInputStream(kapcsolat.getInputStream());
            DataOutputStream szervernek = new DataOutputStream(kapcsolat.getOutputStream());

            Scanner scan = new Scanner(System.in);
            while (true) {
                System.out.print("Kérem a megyét: ");
                String megye = scan.nextLine();
                szervernek.writeUTF(megye);
                szervernek.flush();

                int menu;
                int mikor;
                do{
                    System.out.println("\nVálasszon az alábbi lehetőségek közül: ");
                    System.out.println("1: Maximum\n2: Minimum\n3: Időjárás\n\n4: Kilépés");
                    menu = scan.nextInt();
                    szervernek.writeInt(menu);
                    szervernek.flush();
                    if (menu == 1 || menu == 2 || menu == 3) {
                        System.out.println("1: Mai\n2: Holnapi");
                        mikor = scan.nextInt();
                        szervernek.writeInt(mikor);
                        szervernek.flush();
                    }
                    System.out.println(szervertol.readUTF());
                } while (menu != 4);

            }
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
}
