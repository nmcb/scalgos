package com.github.pathikrit.scalgos

import math.Ordering.Implicits._

import Implicits.Crossable

/**
 * collection of algorithms related to combinatorics
 */
object Combinatorics {

  /**
   * Iterate over all 2^n combinations
   *
   * @param s sequence to do combination over
   * @return all 2^n ways of choosing elements from s
   */
  def combinations[A](s: Seq[A]) = for {i <- 0 to s.length; j <- s combinations i} yield j

  /**
   * Combinations with repeats e.g. (2, Set(A,B,C)) -> AA, AB, AC, BA, BB, BC, CA, CB, CC
   *
   * @return all s.length^n combinations
   */
  def repeatedCombinations[A](s: Set[A], n: Int) : Traversable[List[A]] = n match {
    case 0 => List(Nil)
    case _ => for {(x,xs) <- s X repeatedCombinations(s, n-1)} yield x :: xs
  }

  /**
   * Generates all n^s.length combinations
   * O(n)
   * Think of as instead of boolean (2 states), we have n states for each cell in s
   * Also, equivalent to adding 1 in base n
   * Call with initially all zeroes in s
   *
   * @return next combination of n
   */
  def nextCombination(s: List[Int], n: Int): List[Int] = s match {
    case Nil => Nil
    case x :: xs if x < 0 || x >= n => throw new IllegalArgumentException
    case x :: xs if x == (n-1) => 0 :: nextCombination(xs, n)
    case x :: xs => (x+1) :: xs
  }

  /**
   * Find next permutation of s
   * Call with Seq(0,0,0,0,1,1) for example to 4C2
   * O(n)
   *
   * @return Some(p) if next permutation exists or None if s is already in decreasing order
   */
  def nextPermutation[A : Ordering](s: Seq[A]): Option[Seq[A]] = {
    val pivot = s zip s.tail lastIndexWhere {case (first, second) => first < second}
    if (pivot < 0) {
      None
    } else {
      val next = s lastIndexWhere {_ > s(pivot)}
      // swap the pivot and next, and then reverse the portion of the array to the right of where the pivot was found
      Some(((s take pivot) :+ s(next)) ++ ((s.slice(pivot+1, next):+ s(pivot)) ++ (s drop next+1)).reverse)
    }
  }

  /**
   * @return memoized function to calculate C(n,r)
   */
  val c: Memo[(Int, Int), BigInt] = Memo {
    case (_, 0) => 1
    case (n, r) if r > n/2 => c(n, n-r)
    case (n, r) => c(n-1, r-1) + c(n-1, r)
  }

  /**
   * @return memoized function to calculate n!
   */
  val factorial: Memo[Int, BigInt] = Memo {
    case 0 => 1
    case n => n * factorial(n-1)
  }

  /**
   * Fibonacci number calculator
   * O(n) - each number is calculated once in O(1) time
   *
   * @return memoized function to calculate nth fibonacci number
   */
  val fibonacci: Memo[Int, BigInt] = Memo {
    case 0 => 0
    case 1 => 1
    case n => fibonacci(n-1) + fibonacci(n-2)
  }

  /**
   * Calculate catalan number
   * O(n)
   * A slower relation exists: c(n) = (0 until n) map {i => c(i) * c(n-i-1)} sum
   *
   * @return memoized function to calculate nth catalan number
   */
  val catalan: Memo[Int, BigInt] = Memo {
    case 0 => 1
    case n => (4*n-2)*catalan(n-1)/(n+1)
  }

  /**
   * Number of ways of selecting (1 to n) items such that none of the items are in its own position
   * TODO: Proof
   * TODO: nextPartialDerangement
   * O(n)
   *
   * @return memoized function to count derangements
   */
  val derangement: Memo[Int, BigInt] = Memo {
    case n if n%2 == 0 => n*derangement(n-1) + 1
    case n if n%2 == 1 => n*derangement(n-1) - 1
    case _ => 0     // negative n
  }

  /**
   * @return Number of ways to arrange [1 to n] such that exactly k of them are in own position
   */
  def partialDerangement(n: Int, k: Int) = c(n,k) * derangement(n-k)
}
