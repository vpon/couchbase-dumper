package com.vpon.cbdumper

import java.net.URI

import com.couchbase.client.{CouchbaseClient, CouchbaseConnectionFactory}

object App {

  case class AppConfig(uri: Option[String] = None,
                       bucket: Option[String] = None,
                       password: Option[String] = None,
                       keys: Seq[String] = Vector())

  def main(args: Array[String]) {

    System.setProperty("net.spy.log.LoggerImpl", "net.spy.memcached.compat.log.SLF4JLogger");

    val parser = new scopt.OptionParser[AppConfig]("cbdumer") {
      head("cbdumer", "0.2.0")
      opt[String]('u', "uri") required() action { case   (v, c) =>
        c.copy(uri = Some(v)) } text "Couchbase URI"
      opt[String]('b', "bucket") required() action { case (v, c) =>
        c.copy(bucket = Some(v)) } text "Bucket name"
      opt[String]('p', "password") required() action { case (v, c) =>
        c.copy(password = Some(v)) } text "Bucket password"
      help("help") text "prints this usage text"
      arg[String]("<string>...") unbounded() optional() action { (v, c) =>
        c.copy(keys = c.keys :+ v) } text "Keys"
    }

    try {
      parser.parse(args, AppConfig()) map { config =>
        val baseList = new java.util.ArrayList[URI]()
        baseList.add(URI.create(config.uri.get))
        val cf = new CouchbaseConnectionFactory(baseList, config.bucket.get, config.password.get)
        val cb = new CouchbaseClient(cf)

        config.keys.foreach { key =>
          val res = cb.get(key)
          println(s"$key = ${res}")
        }

        cb.shutdown()

      } getOrElse {
        // arguments are bad, error message will have been displayed
        sys.exit(1)
      }
    } catch {
      case ex:Throwable =>
        println("Error: ", ex)
        sys.exit(2)
    }

  }
}
