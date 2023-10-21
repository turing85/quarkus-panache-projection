package de.turing85.panache.projection;

import de.turing85.panache.projection.entity.Data;
import io.quarkus.panache.common.Sort;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("data")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DataResource {
  @POST
  @Transactional
  public Data save(String name) {
    Data newData = Data.builder().name(name).build();
    newData.persist();
    return newData;
  }

  @GET
  public List<Data> findAll() {
    return Data.listAll(Sort.by("id"));
  }

  @GET
  @Path("ids")
  @Transactional
  public List<Long> findAllIds() {
    return Data.find("SELECT DISTINCT d.id FROM Data d", Sort.by("id"))
        .project(Long.class)
        .list();
  }

  @DELETE
  @Path("{id}")
  @Transactional
  public void delete(@PathParam("id") long id) {
    Data.deleteById(id);
  }
}