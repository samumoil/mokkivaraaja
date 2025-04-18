package com.github.samumoil.mokkivaraaja;

import java.util.List;

public class Main {
    public static void main(String[] args) {
      List<String> names;
      try (DB database = new DB()) {
        names = database.getNames();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }

      for (String name : names) {
        System.out.println(name);
      }
    }
}