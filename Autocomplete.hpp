/**
 *  CSE 100 PA2 C++ Autocomplete
*/

#ifndef AUTOCOMPLETE_HPP
#define AUTOCOMPLETE_HPP

#include <vector>
#include <string>
#include <algorithm>
#include <iostream>
#include "TST.hpp"
using namespace std;

// Object used to compare two pairs
struct comparison {
  bool operator() (pair<string, int>i, pair<string, int>j){ 
    if(i.second != j.second){return i.second > j.second;}
    return(i.first < j.first);
  }
} comparison;

/**
 *  You may implement this class as either a mulit-way trie
 *  or a ternary search trie.
 *
 *  You may not use std::map in this implementation
 */
class Autocomplete
{
public:

  TST trie; // TST structure to hold corpus

  /* 
  Create an Autocomplete object.
  This object should be trained on the corpus vector
  That is - the predictCompletions() function below should pull autocomplete
  suggestions from this vector
  This vector will likely contain duplicates.
  This duplication should be your gauge of frequencey.

  Input: corpus - the corpus of words to learn from.
  Assume preprocessing has been done for you on this! E.g.
  if one of the words is "dán't", assume that each of those characters
  should be included in your trie and don't modify that word any further
  */
  Autocomplete(const vector<string> & corpus){
    for(string word : corpus){
      trie.insert(word);
    }
  }

  /* Return up to 10 of the most frequent completions
   * of the prefix, such that the completions are words in the dictionary.
   * These completions should be listed from most frequent to least.
   * If there are fewer than 10 legal completions, this
   * function returns a vector with as many completions as possible.
   * Otherwise, 10 completions should be returned.
   * If no completions exist, then the function returns a vector of size 0.
   * The prefix itself might be included in the returned words if the prefix
   * is a word (and is among the 10 most frequent completions
   * of the prefix)
   * If you need to choose between two or more completions which have the same frequency,
   * choose the one that comes first in alphabetical order.
   *
   * Inputs: prefix. The prefix to be completed. Must be of length >= 1.
   * Return: the vector of completions
   */
  vector<string> predictCompletions(const string & prefix){
    int idx = 0; // Index in the string
    int len = prefix.length(); // Length of the string
    char ch; // Current char
    TSTNode *n = trie.root;
    vector< pair<string, int> > pairs; // Vector of pointers to pairs in the map
    vector<string> result; // The result of the prefix query

    if(n == nullptr){return result;} // Return empty result when corpus is empty

    while(idx < len){
      ch = prefix.at(idx);

      if(ch < n->data){ // Search to left node
        n = n->left;
      }
      else if(ch > n->data){ // Search to right node
        n = n->right;
      }
      else{ // Node matches
        if(idx == len-1){ // Last char
          if(n->eow){ // The case where the prefix itself is a word
            pairs.push_back(make_pair(prefix, n->freq)); // Add it to the pairs first
          }
          n = n->mid;
          break;
        }
        else{
          n = n->mid;
          idx++;
        }
      }

      if(n == nullptr){ // No node found when traversing through prefix: the prefix does not exist
        return result;
      }
    }

    findHelper(n, prefix, &pairs); // Fill umap with all results with the prefix

    // Vector filling
    std::sort(pairs.begin(), pairs.end(), comparison); // Sort the pairs
    
    int count = 0; // Count of # of elements in the vector
    for(pair<string, int> pair : pairs){ // Fill the vector of strings
      result.push_back(pair.first);
      count++;
      if(count >= 10){break;}
    }
    return result;
  }

  void findHelper(TSTNode *n, string buffer, vector< pair<string, int> > *pairs){
    if(n == nullptr){
      return;
    }
    else{
      findHelper(n->left, buffer, pairs);
      findHelper(n->right, buffer, pairs);
      findHelper(n->mid, buffer + n->data, pairs);
      if(n->eow){
        buffer = buffer + n->data;
        pairs->push_back(make_pair(buffer, n->freq));
      }
    }
  }

  /* Destructor */
  ~Autocomplete(){
    trie.deleteAll(trie.root);
  }
};


#endif // AUTOCOMPLETE_HPP
