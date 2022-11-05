package de.turing85.panache.projection;

import de.turing85.panache.projection.entity.Data;
import io.quarkus.panache.common.Sort;
import java.util.List;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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