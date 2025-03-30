/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */

package ru.numbDev.XmlGenerator.model;

import java.util.Objects;

public record Model(String id, String object, String ownedBy) {

    String getName() {
      return this.id;
    }
  
    boolean isPro() {
      return Objects.equals(getName(), "GigaChat-Pro");
    }
  }