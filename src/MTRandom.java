/********************************************************************************
 * Copyright (c) 2005,2023 David Beaumont (david . beaumont + mtr @ gmail . com)
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0, or the Apache License, Version 2.0
 * which is available at https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
 ********************************************************************************/
package src;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

/**
 * A Java implementation of the MT19937 (Mersenne Twister) pseudo random number generator algorithm
 * based upon the original C code by Makoto Matsumoto and Takuji Nishimura (see <a
 * href="http://www.math.sci.hiroshima-u.ac.jp/m-mat/MT/MT2002/emt19937ar.html">
 * http://www.math.sci.hiroshima-u.ac.jp/m-mat/MT/MT2002/emt19937ar.html</a> for more information).
 *
 * <p>As a subclass of {@link java.util.Random}, this class provides a single canonical {@link
 * Random#next(int)} method for generating bits in the pseudo random number sequence. Anyone using
 * this class should invoke the public inherited methods ({@link Random#nextInt()}, {@link
 * Random#nextFloat()} etc.) to obtain values as normal. This class should provide a drop-in
 * replacement for the standard implementation of {@code java.util.Random} with the additional
 * advantage of having a far longer period and the ability to use a far larger seed value.
 *
 * <p>This is <b>not</b> a cryptographically strong source of randomness and should <b>not</b> be
 * used for cryptographic systems or in any other situation where true random numbers are required.
 *
 * @version 1.1
 * @author David Beaumont, Copyright 2005,2023
 */
public final class MTRandom extends Random {
  // Internal enum to help untangle non-trivial contrustor chains.
  private enum Mode {
    NORMAL,
    COMPATIBILITY
  }

  /**
   * Auto-generated serial version UID. Note that MTRandom does NOT support serialisation of its
   * internal state, and it may even be necessary to implement read/write methods to re-seed it
   * properly. This is only here to make Eclipse shut up about it being missing.
   */
  private static final long serialVersionUID = -515082678588212038L;

  // Constants used in the original C implementation.  The names here match the code in:
  // http://www.math.sci.hiroshima-u.ac.jp/m-mat/MT/MT2002/CODES/mt19937ar.c
  private static final int N = 624;
  private static final int M = 397;
  private static final int[] MATRIX_A = {0x0, 0x9908b0df};
  private static final int UPPER_MASK = 0x80000000;
  private static final int LOWER_MASK = 0x7fffffff;

  // Inlined values extracted as constants.
  private static final int MAGIC_FACTOR1 = 1812433253;
  private static final int MAGIC_FACTOR2 = 1664525;
  private static final int MAGIC_FACTOR3 = 1566083941;
  private static final int MAGIC_MASK1 = 0x9d2c5680;
  private static final int MAGIC_MASK2 = 0xefc60000;
  private static final int MAGIC_SEED = 19650218;
  private static final long LEGACY_DEFAULT_SEED = 5489L;

  // Internal state. The names here match the code in:
  // http://www.math.sci.hiroshima-u.ac.jp/m-mat/MT/MT2002/CODES/mt19937ar.c
  //
  // State vector (length N). This cannot be final due to how java.util.Random is initialized.
  private int[] mt;
  // State vector index (0 <= mti <= N). Note that this *can* obtain the value N temporarily, and
  // indicates that the state vector must be re-processed before the next random value can be taken.
  private int mti;

  // Compatibility mode - used to mimic original C-code exactly (though this is not recommended).
  //
  // WARNING: This is read before it is initialized due to java.util.Random invoking setSeed(long)
  // in the parent constructor. We detect this by checking for {@code null} in our version of
  // setSeed(long), and ignore those calls. We always set a seed value manually later.
  private final Mode compatibilityMode;

  // Private constructor which must be called by all other constructors to indicate subclass
  // initialization has begun.
  private MTRandom(Mode compatibilityMode) {
    super(0L);
    this.compatibilityMode = compatibilityMode;
  }

  /**
   * Creates a new {@code MTRandom} instance. This constructor sets the seed of the random number
   * generator to a value very likely to be distinct from any other invocation of this constructor.
   *
   * <p>Note that the method to generate a default seed is explicitly not defined and may change
   * without notice as improvements are made. If you need to rely on deterministic output, you
   * should always set seed data explicitly.
   */
  public MTRandom() {
    this(false);
  }

  /**
   * This version of the constructor can be used to implement identical behaviour to the original C
   * code version of this algorithm including exactly replicating the case where the seed value had
   * not been set prior to calling {@code genrand_int32}.
   *
   * <p>If {@code compatible} is set to {@code true}, then the algorithm will be seeded with the
   * same default value as was used in the original C code. Furthermore, the {@link
   * Random#setSeed(long)} method, which must take a 64 bit {@code long} value, will be limited to
   * using only the lower 32 bits of the seed to facilitate seamless migration of existing C code
   * into Java where identical behaviour is required.
   *
   * <p>While useful for ensuring backwards compatibility, it is advised that this feature not be
   * used unless specifically required, due to the reduction in strength of the seed value.
   *
   * <p>If {@code compatible} is set to {@code false}, this constructor behaves exactly the same as
   * {@link #MTRandom()}.
   *
   * @param compatible Compatibility flag for replicating original C behaviour.
   */
  public MTRandom(boolean compatible) {
    this(compatible ? Mode.COMPATIBILITY : Mode.NORMAL);
    // Always call setSeed *after* setting compatibility mode.
    setSeed(compatible ? LEGACY_DEFAULT_SEED : bestEffortUniqueSeed());
  }

  /**
   * This version of the constructor simply initialises the class with the given 64 bit seed value.
   * For a better random number sequence this seed value should contain as much entropy as possible.
   *
   * @param seed The seed value with which to initialise this class.
   */
  public MTRandom(long seed) {
    this(Mode.NORMAL);
    // Always call setSeed *after* setting compatibility mode.
    setSeed(seed);
  }

  /**
   * This version of the constructor initialises the class with the given byte array. All the data
   * will be used to initialise this instance.
   *
   * @param seedBytes The non-empty byte array of seed information.
   * @throws NullPointerException if the buffer is null.
   * @throws IllegalArgumentException if the buffer has zero length.
   */
  public MTRandom(byte[] seedBytes) {
    this(Mode.NORMAL);
    // Always call setSeed *after* setting compatibility mode.
    setSeed(seedBytes);
  }

  /**
   * This version of the constructor initialises the class with the given integer array. All the
   * data will be used to initialise this instance.
   *
   * @param seedArray The non-empty integer array of seed information.
   * @throws NullPointerException if the buffer is null.
   * @throws IllegalArgumentException if the buffer has zero length.
   */
  public MTRandom(int[] seedArray) {
    this(Mode.NORMAL);
    // Always call setSeed *after* setting compatibility mode.
    setSeed(seedArray);
  }

  // Initializes the state vector from a simple integer seed. This method is
  // required as part of the Mersenne Twister algorithm but need not be made public.
  //
  // Note that every element in the state vector is written sequentially, and later
  // values are a function of earlier ones. This makes it unnecessary to zero the
  // state vector if setSeed() is called repeatedly.
  private void initStateVector(int seed) {
    // Annoying runtime check for initialisation of internal data caused by java.util.Random
    // invoking setSeed() during init. This is unavoidable because no fields in our instance will
    // have been initialised at this point, not even if the code were placed at the declaration of
    // the member variable.
    if (mt == null) mt = new int[N];

    // Unlike the C-code, this version uses a local variable for the loop rather than directly
    // modifying the state vector index field. This means we must also set the state vector index
    // manually.
    // ---- Begin Mersenne Twister Algorithm ----
    mt[0] = seed;
    for (int n = 1; n < N; n++) {
      int prev = mt[n - 1];
      mt[n] = (MAGIC_FACTOR1 * (prev ^ (prev >>> 30)) + n);
    }
    mti = N;
    // ---- End Mersenne Twister Algorithm ----
  }

  /**
   * {@inheritDoc}
   *
   * <p>This method resets the state of this instance using the 64 bits of seed data provided. Note
   * that if the same seed data is passed to two different instances of MTRandom (both of which
   * share the same compatibility state) then the sequence of numbers generated by both instances
   * will be identical.
   *
   * <p>If this instance was initialised in 'compatibility' mode then this method will only use the
   * lower 32 bits of any seed value passed in and will match the behaviour of the original C code
   * exactly with respect to state initialisation.
   *
   * @param seed The 64 bit value used to initialise the random number generator state.
   */
  @Override
  public synchronized void setSeed(long seed) {
    if (compatibilityMode == null) {
      // This call came from the parent class constructor and should be ignored since we will be
      // resetting the seed manually in a moment anyway.
      return;
    }
    if (compatibilityMode == Mode.COMPATIBILITY) {
      // Ignore upper 32-bits of seed in compatibility mode.
      initStateVector((int) seed);
    } else {
      setSeed(new int[] {(int) seed, (int) (seed >>> 32)});
    }
  }

  /**
   * This method resets the state of this instance using the byte array of seed data provided. Note
   * that calling this method is equivalent to calling {@code setSeed(pack(seedBytes))} and in
   * particular will result in a new integer array being generated during the call. If you wish to
   * retain this seed data to allow the pseudo random sequence to be restarted then it would be more
   * efficient to use the {@link #pack(byte[])} method to convert it into an integer array first,
   * and then use that to re-seed the instance.
   *
   * @param seedBytes The non-empty byte array of seed information.
   * @throws NullPointerException if the buffer is null.
   * @throws IllegalArgumentException if the buffer has zero length.
   */
  public void setSeed(byte[] seedBytes) {
    setSeed(pack(seedBytes));
  }

  /**
   * This method resets the state of this instance using the integer array of seed data provided.
   * This is the canonical way of resetting the pseudo random number sequence.
   *
   * @param seedArray The non-empty integer array of seed information.
   * @throws NullPointerException if the buffer is null.
   * @throws IllegalArgumentException if the buffer has zero length.
   */
  public synchronized void setSeed(int[] seedArray) {
    int length = seedArray.length;
    if (length == 0) throw new IllegalArgumentException("Seed buffer cannot not be empty");
    // ---- Begin Mersenne Twister Algorithm ----
    int i = 1, j = 0, k = Math.max(N, length);
    initStateVector(MAGIC_SEED);
    for (; k > 0; k--) {
      mt[i] = (mt[i] ^ ((mt[i - 1] ^ (mt[i - 1] >>> 30)) * MAGIC_FACTOR2)) + seedArray[j] + j;
      i++;
      j++;
      if (i >= N) {
        mt[0] = mt[N - 1];
        i = 1;
      }
      if (j >= length) j = 0;
    }
    for (k = N - 1; k > 0; k--) {
      mt[i] = (mt[i] ^ ((mt[i - 1] ^ (mt[i - 1] >>> 30)) * MAGIC_FACTOR3)) - i;
      i++;
      if (i >= N) {
        mt[0] = mt[N - 1];
        i = 1;
      }
    }
    mt[0] = UPPER_MASK; // MSB is 1; assuring non-zero initial array
    // ---- End Mersenne Twister Algorithm ----
  }

  /**
   * {@inheritDoc}
   *
   * <p>This method forms the basis for generating a pseudo random number sequence from this class.
   * If given a value of 32, this method behaves identically to the genrand_int32 function in the
   * original C code and ensures that using the standard nextInt() function (inherited from Random)
   * we are able to replicate behaviour exactly.
   *
   * <p>Note that where the number of bits requested is not equal to 32 then bits will simply be
   * masked out from the top of the returned integer value. That is to say that:
   *
   * <pre>{@code
   * mt.setSeed(12345);
   * int foo = mt.nextInt(16) + (mt.nextInt(16) << 16);
   * }</pre>
   *
   * will not give the same result as
   *
   * <pre>{@code
   * mt.setSeed(12345);
   * int foo = mt.nextInt(32);
   * }</pre>
   */
  @Override
  protected synchronized int next(int bits) {
    // ---- Begin Mersenne Twister Algorithm ----
    int y, kk;
    if (mti >= N) { // generate N words at one time

      // In the original C implementation, mti is checked here to determine if initialisation has
      // occurred; if not it initialises this instance with DEFAULT_SEED (5489). This is no longer
      // necessary as initialisation of the Java instance must result in initialisation occurring
      // Use the constructor MTRandom(true) to enable backwards compatible behaviour.

      for (kk = 0; kk < N - M; kk++) {
        y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
        mt[kk] = mt[kk + M] ^ (y >>> 1) ^ MATRIX_A[y & 0x1];
      }
      for (; kk < N - 1; kk++) {
        y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
        mt[kk] = mt[kk + (M - N)] ^ (y >>> 1) ^ MATRIX_A[y & 0x1];
      }
      y = (mt[N - 1] & UPPER_MASK) | (mt[0] & LOWER_MASK);
      mt[N - 1] = mt[M - 1] ^ (y >>> 1) ^ MATRIX_A[y & 0x1];

      mti = 0;
    }

    y = mt[mti++];

    // Tempering
    y ^= (y >>> 11);
    y ^= (y << 7) & MAGIC_MASK1;
    y ^= (y << 15) & MAGIC_MASK2;
    y ^= (y >>> 18);
    // ---- End Mersenne Twister Algorithm ----
    return (y >>> (32 - bits));
  }

  /**
   * Packs a byte array of seed data into a more efficient int array.
   *
   * <p>This simple utility method can be used in cases where a byte array of seed data is to be
   * used to repeatedly re-seed the random number sequence. Packing the byte array into an integer
   * array first using this method, and then invoking {@link #setSeed(int[])} with that, removes the
   * need to re-pack the byte array each time {@link #setSeed(byte[])} is called.
   *
   * <p>If the length of the byte array is not a multiple of 4 then it is implicitly padded with
   * zeros as necessary. For example:
   *
   * <pre>    byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06 }</pre>
   *
   * becomes:
   *
   * <pre>    int[]  { 0x04030201, 0x00000605 }</pre>
   *
   * <p>Note that this method will not complain if the given byte array is empty and will produce an
   * empty integer array, but the {@code setSeed()} method will throw an exception if the empty
   * integer array is passed to it.
   *
   * @param bytes The non-null byte array to be packed.
   * @return An integer array of packed bytes.
   * @throws NullPointerException if the given byte array is null.
   */
  public static int[] pack(byte[] bytes) {
    int k, blen = bytes.length, ilen = ((bytes.length + 3) >>> 2);
    int[] ibuf = new int[ilen];
    for (int n = 0; n < ilen; n++) {
      int m = (n + 1) << 2;
      if (m > blen) m = blen;
      for (k = bytes[--m] & 0xff; (m & 0x3) != 0; ) {
        k = (k << 8) | bytes[--m] & 0xff;
      }
      ibuf[n] = k;
    }
    return ibuf;
  }

  // Shared value used to attempt to ensure uniqueness of default seeds.
  private static final AtomicLong defaultSeed = new AtomicLong();

  // Internal method to generate a "probably unique" seed array with a "reasonable" number of bits
  // based on the system time and accumulated state.
  //
  // The magic multiplier "4292484099903637661" is obtained from "Table 5" in:
  // https://www.ams.org/mcom/1999-68-225/S0025-5718-99-00996-5/S0025-5718-99-00996-5.pdf
  // and listed as a good accumulator for a 64-bit modulus (when no constant adjustment is used).
  private static long bestEffortUniqueSeed() {
    return defaultSeed.accumulateAndGet(
        System.nanoTime(), (s, t) -> (s ^ t) * 4292484099903637661L);
  }

  // Synchronized to ensure we only snapshot valid state.
  private synchronized void writeObject(ObjectOutputStream stream) throws IOException {
    stream.defaultWriteObject();
  }

  private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
    stream.defaultReadObject();
    // Since you should not be able to serialize the instance until it's constructed, we expect
    // internal invariants to be maintained.
    //
    // Note that while "mti == mt.length" is possible during nextInt(), that method is synchronized
    // and snapshots can only be serialized when it's < my.length.
    if (mt == null || mti < 0 || mti >= mt.length || compatibilityMode == null) {
      throw new IOException("Invalid internal state for MTRandom");
    }
  }
}
