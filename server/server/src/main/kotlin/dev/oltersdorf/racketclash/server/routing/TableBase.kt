package dev.oltersdorf.racketclash.server.routing

import dev.oltersdorf.racketclash.database.api.IdItem
import dev.oltersdorf.racketclash.database.api.TableBase
import dev.oltersdorf.racketclash.server.api.SortedAndFilteredQuery
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route

internal inline fun <reified Item : IdItem, reified Filter, reified Sorting> Route.tableBaseRoutes(
    path: String,
    table: TableBase<Item, Filter, Sorting>
) {
    route(path) {
        //insert
        post {
            val item = call.receive<Item>()
            val id = table.insert(item)
            call.respond(HttpStatusCode.Created, id)
        }

        //delete
        delete("/{id}") {
            val id = call.parameters["id"]?.toLongOrNull() ?: return@delete call.respond(HttpStatusCode.BadRequest)
            val wasSuccess = table.delete(id)
            if (wasSuccess)
                call.respond(HttpStatusCode.OK)
            else
                call.respond(HttpStatusCode.NotFound)
        }

        //update
        put {
            val item = call.receive<Item>()
            val wasSuccess = table.update(item)
            if (wasSuccess)
                call.respond(HttpStatusCode.OK)
            else
                call.respond(HttpStatusCode.NotFound)
        }

        //selectSingle
        get("/{id}") {
            val id = call.parameters["id"]?.toLongOrNull() ?: return@get call.respond(HttpStatusCode.BadRequest)
            val item = table.selectSingle(id)
            if (item == null)
                call.respond(HttpStatusCode.NotFound)
            else
                call.respond(item)
        }

        //selectLast
        get("/last/{n}") {
            val n = call.parameters["n"]?.toIntOrNull() ?: return@get call.respond(HttpStatusCode.BadRequest)
            call.respond(table.selectLast(n))
        }

        //selectSortedAndFiltered
        post("/list") {
            val request = call.receive<SortedAndFilteredQuery<Filter, Sorting>>()

            val result = table.selectSortedAndFiltered(
                request.filter,
                request.sorting,
                request.fromIndex,
                request.limit
            )

            call.respond(result)
        }
    }
}