package com.example.exceptions;

/**
 * Created by Sam on 11/4/2016.
 */
public class CardNotAcceptedException extends RuntimeException {

        public CardNotAcceptedException(String message) {
            super(message);
        }

}
