package com.hwrec

import java.net.InetAddress
import java.util.concurrent.Executors

import akka.actor.{ ActorRef, ActorSystem }
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer

import scala.concurrent.duration.Duration
import scala.concurrent.{ Await, ExecutionContext, Future }
import scala.util.{ Failure, Success }

object HttpServer extends App with Routes with Recognizer with Differentiator with Data with DB {

  val k: Int = 15

  implicit val system: ActorSystem = ActorSystem("HwrecHttpServer")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  //  implicit val executionContext: ExecutionContext = system.dispatcher
  implicit val executionContext: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global
  //  implicit val executionContext: ExecutionContext = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(Runtime.getRuntime.availableProcessors()))

  val userRegistryActor: ActorRef = system.actorOf(UserRegistryActor.props, "userRegistryActor")

  // val interface = "localhost" // InetAddress.getLocalHost.getHostAddress
  val interface = InetAddress.getLocalHost.getHostAddress
  val serverBinding: Future[Http.ServerBinding] = Http().bindAndHandle(routes, interface, 4000)
  log.info("Routes: {}", routes)

  serverBinding.onComplete {
    case Success(bound) =>
      println(s"Server online at http://${bound.localAddress.getHostString}:${bound.localAddress.getPort}/")
    case Failure(e) =>
      Console.err.println(s"Server could not start!")
      e.printStackTrace()
      system.terminate()
  }

  Await.result(system.whenTerminated, Duration.Inf)
}
