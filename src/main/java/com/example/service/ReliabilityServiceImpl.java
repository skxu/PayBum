package com.example.service;

import org.springframework.stereotype.Service;

/**
 * Created by Sam on 11/4/2016.
 */
@Service
public class ReliabilityServiceImpl implements ReliabilityService {

    @Override
    public boolean isReliable() {
        // fails on average 9 out of 10 times
        return Math.random() < 0.1;
    }
}
