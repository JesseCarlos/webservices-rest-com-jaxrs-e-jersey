package br.com.alura.loja.resource;

import br.com.alura.loja.dao.ProjetoDAO;
import br.com.alura.loja.modelo.Projeto;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

@Path("projetos")
public class ProjetoResource {

    private ProjetoDAO projetoDAO = new ProjetoDAO();

    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Projeto busca(@PathParam("id") long id) {
        return projetoDAO.busca(id);
    }

    @POST
    @Consumes(MediaType.APPLICATION_XML)
    public Response adiciona(Projeto projeto) {
        projetoDAO.adiciona(projeto);

        final URI location = URI.create("/projetos/" + projeto.getId());
        return Response.created(location).build();
    }

    @Path("{id}/remove")
    @DELETE
    public Response remove(@PathParam("id") long id) {
        projetoDAO.remove(id);
        return Response.ok().build();
    }
}
