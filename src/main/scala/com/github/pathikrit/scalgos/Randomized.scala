package com.github.pathikrit.scalgos

import util.Random
import math.Ordering.Implicits._

/**
 * Collection of randomized algorithms
 */
object Randomized {

  /**
   * Randomized algorithm to select the kth item in a sequence in amortised linear time
   *
   * @return the k-th item in s
   */
  def quickSelect[T: Ordering](s: Seq[T], k: Int): T = {
    assume(k >= 0 && k < s.size)
    val pivot = s(Random.nextInt(s.length))
    val (low, rest) = s partition {_ < pivot}
    if (k < low.size) {
      quickSelect(low, k)
    } else {
      val (equal, high) = rest partition {_ == pivot}
      if (k < low.size + equal.size) pivot else quickSelect(high, k - low.size - equal.size)
    }
  }

  /**
   * A Park-Miller Pseudo-random number generator (minimum standard one)
   *
   * @param i the seed e.g. use i = pring(i)
   * @return a pseudo-random number
   */
  def minstd(i: Int) = (i*16807) % Int.MaxValue
}
