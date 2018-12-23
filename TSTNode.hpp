#ifndef TSTNODE_HPP
#define TSTNODE_HPP
#include <iostream>
using namespace std;

class TSTNode {
   public:
    TSTNode *left;
    TSTNode *right;
    TSTNode *mid;
    char data;
    int freq; // Frequency of the word
    bool eow; // End of the word

    TSTNode(char d){
    	data = d; 
    	left = right = mid = 0;
    	freq = 0;
    	eow = false;
    }
};

#endif