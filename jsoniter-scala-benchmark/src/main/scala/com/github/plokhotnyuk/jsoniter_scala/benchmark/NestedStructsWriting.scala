package com.github.plokhotnyuk.jsoniter_scala.benchmark

import java.nio.charset.StandardCharsets.UTF_8

import com.avsystem.commons.serialization.json._
import com.github.plokhotnyuk.jsoniter_scala.benchmark.AVSystemCodecs._
//import com.github.plokhotnyuk.jsoniter_scala.benchmark.BorerJsonEncodersDecoders._
import com.github.plokhotnyuk.jsoniter_scala.benchmark.CirceEncodersDecoders._
import com.github.plokhotnyuk.jsoniter_scala.benchmark.JacksonSerDesers._
import com.github.plokhotnyuk.jsoniter_scala.benchmark.JsoniterScalaCodecs._
import com.github.plokhotnyuk.jsoniter_scala.benchmark.PlayJsonFormats._
import com.github.plokhotnyuk.jsoniter_scala.benchmark.SprayFormats._
import com.github.plokhotnyuk.jsoniter_scala.benchmark.UPickleReaderWriters._
import com.github.plokhotnyuk.jsoniter_scala.core._
import io.circe.generic.auto._
import io.circe.syntax._
import org.openjdk.jmh.annotations.Benchmark
import play.api.libs.json.Json
import spray.json._

class NestedStructsWriting extends NestedStructsBenchmark {
  @Benchmark
  def avSystemGenCodec(): Array[Byte] = JsonStringOutput.write(obj).getBytes(UTF_8)
/* FIXME: Borer throws io.bullet.borer.Borer$Error$General: java.lang.NullPointerException (Output.ToByteArray index 4)
  @Benchmark
  def borerJson(): Array[Byte] = io.bullet.borer.Json.encode(obj).toByteArray
*/
  @Benchmark
  def circe(): Array[Byte] = printer.pretty(obj.asJson).getBytes(UTF_8)
/* FIXME: DSL-JSON serializes null value for Option.None
  @Benchmark
  def dslJsonScala(): Array[Byte] = dslJsonEncode(obj)
*/
  @Benchmark
  def jacksonScala(): Array[Byte] = jacksonMapper.writeValueAsBytes(obj)

  @Benchmark
  def jsoniterScala(): Array[Byte] = writeToArray(obj)

  @Benchmark
  def jsoniterScalaPrealloc(): Int = writeToSubArray(obj, preallocatedBuf, 0, preallocatedBuf.length)

  @Benchmark
  def playJson(): Array[Byte] = Json.toBytes(Json.toJson(obj))

  @Benchmark
  def sprayJson(): Array[Byte] = obj.toJson(nestedStructsJsonFormat).compactPrint.getBytes(UTF_8)

  @Benchmark
  def uPickle(): Array[Byte] = write(obj).getBytes(UTF_8)
}