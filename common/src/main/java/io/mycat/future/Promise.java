/*
 * Copyright (c) 2011-2019 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0, or the Apache License, Version 2.0
 * which is available at https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
 */
package io.mycat.future;


import static io.mycat.future.Future.factory;

/**
 * Represents the writable side of an action that may, or may not, have occurred yet.
 * <p>
 * The {@link #future()} method returns the {@link Future} associated with a promise, the future
 * can be used for getting notified of the promise completion and retrieve its value.
 * <p>
 * A promise extends {@code Handler<AsyncResult<T>>} so it can be used as a callback.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */

public interface Promise<T> extends Handler<AsyncResult<T>> {

  /**
   * Create a succeeded promise with a {@code null} result
   *
   * @param <T>  the result type
   * @return  the promise
   */
  static <T> Promise<T> succeededPromise() {
    return factory.succeededPromise();
  }

  /**
   * Created a succeeded promise with the specified {@code result}.
   *
   * @param result  the result
   * @param <T>  the result type
   * @return  the promise
   */
  static <T> Promise<T> succeededPromise(T result) {
    return factory.succeededPromise(result);
  }

  /**
   * Create a failed promise with the specified failure {@code cause}.
   *
   * @param cause  the failure cause as a Throwable
   * @param <T>  the result type
   * @return  the promise
   */
  static <T> Promise<T> failedPromise(Throwable cause) {
    return factory.failedPromise(cause);
  }

  /**
   * Create a failed promise with the specified {@code failureMessage}.
   *
   * @param failureMessage  the failure message
   * @param <T>  the result type
   * @return  the promise
   */
  static <T> Promise<T> failedPromise(String failureMessage) {
    return factory.failurePromise(failureMessage);
  }

  /**
   * Create a promise that hasn't completed yet
   *
   * @param <T>  the result type
   * @return  the promise
   */
  static <T> Promise<T> promise() {
    return factory.promise();
  }

  /**
   * Succeed or fail this promise with the {@link AsyncResult} event.
   *
   * @param asyncResult the async result to handle
   */
  @Override
  void handle(AsyncResult<T> asyncResult);

  /**
   * Set the result. Any handler will be called, if there is one, and the promise will be marked as completed.
   * <p/>
   * Any handler set on the associated promise will be called.
   *
   * @param result  the result
   * @throws IllegalStateException when the promise is already completed
   */
  void complete(T result);

  /**
   * Calls {@code complete(null)}
   *
   * @throws IllegalStateException when the promise is already completed
   */
  void complete();

  /**
   * Set the failure. Any handler will be called, if there is one, and the future will be marked as completed.
   *
   * @param cause  the failure cause
   * @throws IllegalStateException when the promise is already completed
   */
  void fail(Throwable cause);

  /**
   * Calls {@link #fail(Throwable)} with the {@code message}.
   *
   * @param message  the failure message
   * @throws IllegalStateException when the promise is already completed
   */
  void fail(String message);

  /**
   * Like {@link #complete(Object)} but returns {@code false} when the promise is already completed instead of throwing
   * an {@link IllegalStateException}, it returns {@code true} otherwise.
   *
   * @param result  the result
   * @return {@code false} when the future is already completed
   */
  boolean tryComplete(T result);

  /**
   * Calls {@code tryComplete(null)}.
   *
   * @return {@code false} when the future is already completed
   */
  boolean tryComplete();

  /**
   * Like {@link #fail(Throwable)} but returns {@code false} when the promise is already completed instead of throwing
   * an {@link IllegalStateException}, it returns {@code true} otherwise.
   *
   * @param cause  the failure cause
   * @return {@code false} when the future is already completed
   */
  boolean tryFail(Throwable cause);

  /**
   * Calls {@link #fail(Throwable)} with the {@code message}.
   *
   * @param message  the failure message
   * @return false when the future is already completed
   */
  boolean tryFail(String message);

  /**
   * @return the {@link Future} associated with this promise, it can be used to be aware of the promise completion
   */

  Future<T> future();

}
