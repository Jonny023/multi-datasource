package com.test

class Two {

    String twoName

    static constraints = {

    }

    Two(String twoName) {
        this.twoName = twoName
    }

    static mapping = {
        datasource "two"
    }
}
