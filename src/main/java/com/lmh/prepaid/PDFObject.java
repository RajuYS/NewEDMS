package com.lmh.prepaid;

import java.util.List;

public class PDFObject
{
  private String a1;
  private String h1;
  private List<String> d1;
  private String d2;
  private String d3;
  private String d4;
  
  public PDFObject() {}
  
  public PDFObject(String a1, String h1, List<String> d1, String d2, String d3, String d4)
  {
    this.a1 = a1;
    this.h1 = h1;
    this.d1 = d1;
    this.d2 = d2;
    this.d3 = d3;
    this.d4 = d4;
  }
  
  public String getA1()
  {
    return this.a1;
  }
  
  public void setA1(String a1)
  {
    this.a1 = a1;
  }
  
  public String getH1()
  {
    return this.h1;
  }
  
  public void setH1(String h1)
  {
    this.h1 = h1;
  }
  
  public List<String> getD1()
  {
    return this.d1;
  }
  
  public void setD1(List<String> d1)
  {
    this.d1 = d1;
  }
  
  public String getD2()
  {
    return this.d2;
  }
  
  public void setD2(String d2)
  {
    this.d2 = d2;
  }
  
  public String getD3()
  {
    return this.d3;
  }
  
  public void setD3(String d3)
  {
    this.d3 = d3;
  }
  
  public String getD4()
  {
    return this.d4;
  }
  
  public void setD4(String d4)
  {
    this.d4 = d4;
  }
  
  @Override
public String toString()
  {
    return "{" + this.a1 + this.h1 + this.d1 + this.d2 + this.d3 + this.d4 + "}";
  }
}
