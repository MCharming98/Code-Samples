#ifndef TST_HPP
#define TST_HPP
#include <iostream>
#include <vector>
#include <string>
#include "TSTNode.hpp"
using namespace std;

class TST {
   public:
    TSTNode *root;

    /* Default constructor. */
    TST() : root(0) {}

    void insert(string word){
        insertHelper(&root, word, 0);
    }

    void insertHelper(TSTNode **n, string word, unsigned int idx) { 
        char ch = word.at(idx);
        // Base Case: Node is empty 
        if ((*n) == nullptr){
            *n = new TSTNode(ch);
        } 
        // Searching left
        if (ch < (*n)->data){ 
            insertHelper(&( (*n)->left ), word, idx); 
        }
        // Searching right
        else if (ch > (*n)->data){ 
            insertHelper(&( (*n)->right ), word, idx); 
        }
        // Char found
        else{ 
            // While the char is not the last one, continue traversing
            if (idx < word.length()-1){ 
                insertHelper(&( (*n)->mid ), word, idx+1); 
            }
            // the last character of the word, set eow and increment freq
            else{
                (*n)->eow = true;
                (*n)->freq++;
            }
        }
    }

    void deleteAll(TSTNode *n){
        if(n == nullptr){
            return;
        }
        deleteAll(n->left);
        deleteAll(n->mid);
        deleteAll(n->right);
        delete(n);
    }
};

#endif