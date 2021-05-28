package com.ibirdman.aidldemo.impl;
import com.ibirdman.aidldemo.model.Advert;

interface IAdvertManager{
    List getAdvertList();

    void addAdvert(in Advert ad);
}
