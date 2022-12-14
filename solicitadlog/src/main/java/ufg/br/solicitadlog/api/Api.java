package ufg.br.solicitadlog.api;



import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.hibernate.query.criteria.internal.expression.function.TrimFunction;
import org.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.com.caelum.stella.ValidationMessage;
import br.com.caelum.stella.validation.CPFValidator;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import ufg.br.solicitadlog.classe.Orgao;
import ufg.br.solicitadlog.classe.SolicitacaoFant;
import ufg.br.solicitadlog.classe.Usuario_Externo;
import ufg.br.solicitadlog.classe.solicitacao;
import ufg.br.solicitadlog.repository.*;


@Controller
public class Api {
	//linha abaixo com variaveis para receber os dados da api
	  String nome; String cpf1;List<String> vinculo = null;List<String> categoria = null;List<Integer> matricula = null;String situacao;Integer ano_ingersso;
	  List<String> unidade_vinculo = null;List<Integer> id_unidade = null;List<String> sigla_unidade = null;String cidade; List<String> sigla_Referencia = null;
	  String access; String scope;String token_type;Integer expire;
	  String unidade;
	  String CPF;
	  String mensagem;
	  Orgao orgao = new Orgao();
	  Usuario_Externo ue = new Usuario_Externo();
	  Long[] cd_orgao;
	  ArrayList<Orgao> listaorgao = new ArrayList<>();
	  Usuario_Externo user = new Usuario_Externo();
	  JSONObject json = new JSONObject();
	  JSONParser ps = new JSONParser();
	@Autowired
	private UsuarioExternoRepository usuarioExternoRepository;
	@Autowired
	private OrgaoRepository orgaoRepository;
	@Autowired
	private EspecieRepository especieRepository;
	@Autowired
	private RequisicaoRepository requisicaoRepository;
	@Autowired
	private SolicitacaoRepository solicitacaoRepository;
	@Autowired
	private SolicitacaoFantRepository solicitacaoFantRepository;
	
	@GetMapping("**/inicio")
	 public ModelAndView inicio() {
		 ModelAndView mv = new ModelAndView("acesso.html");
	     mv.addObject("nome", "");
	     mv.addObject("cpf","");
	     mv.addObject("msg","");
	     mv.addObject("siglaref", listaorgao);
	     mv.addObject("sigla", "");
	     mv.addObject("especie",especieRepository.findAll());
	     mv.addObject("requisicao",requisicaoRepository.findAll());
	     mv.addObject("lista", new SolicitacaoFant());
	     mv.addObject("vcpf",1);
		 return mv;
	 }
	
	  @GetMapping("**/acesso")	
	  public ModelAndView Acesso(@RequestParam("cpf") String cpf) throws ParseException   {
	      //Linha abaixo com variaveis para receber o token de acesso
		  ModelAndView mv = new ModelAndView("acesso.html");
		  CPFValidator cpfvalidator = new CPFValidator();
     	  List<ValidationMessage> erro = cpfvalidator.invalidMessagesFor(cpf);
     	  
     	 if (erro.size()>0) {
     		mensagem="CPF invalido";
     		mv.addObject("msg", mensagem);
     	 }else {
     		    ue=usuarioExternoRepository.verExistenciaLocalDeUsuarioExterno(cpf);
     		    if (Objects.isNull(ue)) {
     		    	mensagem="Usuario com primeiro acesso ou inexistente";
     		    	mv.addObject("msg", mensagem);
     		    	HttpResponse<String> response = Unirest.post("https://data.api.ufg.br/token")
     						  .header("content-type", "application/x-www-form-urlencoded")
     						  .body("grant_type=client_credentials&client_id=IJzJzes8Y06GzZ52b8sT10F3uNIa&client_secret=R9jXFT1NQXZz799BYFYYWXOWGdka&audience=YOUR_API_IDENTIFIER")
     						  .asString();  
     		    	json =  (JSONObject) ps.parse(response.getBody().toString());
     		          access = (String) json.get("access_token");
     		          scope = (String) json.get("scope");
     		          token_type = (String) json.get("token_type");
     		          expire = (Integer) json.get("expires_in");
     		          org.json.JSONObject tudo =null;
     		          try {
       	                  HttpResponse<String> responseuser = Unirest.get("https://data.api.ufg.br/pessoas/comuns/1.0.0/vinculo_ufg_dlog?cpf="+cpf)
       	   			           .header("content-type", "application/json")
       	   			           .header("authorization", token_type+" "+access)
       	   			           .asString();
       	   			     tudo = new org.json.JSONObject(responseuser.getBody().toString());      
       	   			     json = (JSONObject) ps.parse(responseuser.getBody().toString());
       	   			     JSONArray array = tudo.getJSONArray("vinculos");
       	   	             nome=(String) json.get("nome");
                         CPF=(String) json.get("cpf");
                         if (array.length()>0) {
                        	 for(int i=0;i<array.length();i++) {
                        		 org.json.JSONObject f =array.getJSONObject(i);
                        		 matricula.add(f.getInt("matricula"));
                        		 categoria.add(f.getString("categoria"));
                        		 ano_ingersso=(f.getInt("ano_ingresso"));
                        		 cidade=f.getString("cidade_unidade_vinculo");
                        		 situacao=f.getString("situacao");
                        		 vinculo.add(f.getString("vinculo"));
                        		 sigla_unidade.add(f.getString("sigla_unidade_vinculo"));
                        		 listaorgao =  (ArrayList<Orgao>) orgaoRepository.pesquisaOrgaoPorSigla(f.getString("sigla_unidade_vinculo"));
                        		 cd_orgao=orgaoRepository.buscaCodigoPorSg_orgao(f.getString("sigla_unidade_vinculo"));
                        		 unidade=(f.getString("unidade_vinculo"));
                        	     mensagem="Usuario novo encontrado "+json.get("nome")+" com vinculos: "+f.getString("unidade_vinculo");	 
                        	     mv.addObject("msg", mensagem);
                        	 }
                         }else {
                        	 mensagem="Nenhum usuario encontrado com este cpf";
                        	 mv.addObject("msg", mensagem); 
                         }
       	   			     
     		          }catch (Exception e) {
     		        	       mensagem="Nada foi encontrado para este cpf";
     		        	       mv.addObject("msg", mensagem);
						
					}
     		         Unirest.shutDown();
     		    	
     		    }else {
     		    	   mensagem="Usuario com alguma frequencia";
     		    	   mv.addObject("msg", mensagem);
     		    }
     	 }
     	 
     	 
         return mv;  
	  }
	  @PostMapping("**/salvarsolicitacao")
	  public ModelAndView Salvar(@RequestParam("especie")Integer especie,@RequestParam("ini")String ini,@RequestParam("fim")String fim,
			  @RequestParam("destino") String destino,@RequestParam("motivo")String motivo,@RequestParam("requisicao")Long requisicao,
			  @RequestParam("contato")String contato) throws java.text.ParseException {
		  ModelAndView mv = new ModelAndView("solicitacao.html");
		  Long id = solicitacaoRepository.verMaximoId();
		  
		  System.out.println("Testando o post: "+nome+" cpf "+CPF);
		  if (id==null) {
			  id=(long) 1;
		  }else {
			  id=id+1;
		  }
		  if (especie!=null) {
			  if (!ini.equals("")&&!ini.equals(null)) {
				  if (!fim.equals("")&&!fim.equals(null)){
					  if (!destino.equals("")&&!destino.equals(null)) {
						  if (!motivo.equals("")&&!motivo.equals(null)) {
							  if(requisicao!=null) {
								  if(!contato.equals("")&&!contato.equals(null)) {
									  if(!nome.equals(null)) {
									  solicitacao sol = new solicitacao();
									  sol.setEspecie(especie);
									  sol.setCpf_solicitante(CPF);
									  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
									  sol.setData_ini(formatter.parse(ini));
									  sol.setData_fim(formatter.parse(fim));
									  sol.setDestino(destino);
									  sol.setMotivo(motivo);
									  sol.setId(id);
									  sol.setNome_unidade(ue.getUnidade_vinculo());
									  sol.setRequisicao(requisicao);									  
									  sol.setSolicitante(nome);
	
								      sol.setContato(contato);
									  sol.setData_solicitacao(new Date());
									/*  for(Long c:cd_orgao) {
									  sol.setUnidade(c);
									  }*/
									  sol.setUnidade(ue.getId_unidade());
									  solicitacaoRepository.save(sol);
									  mv.addObject("msg", "Solicitação realizada com sucesso com ID= "+id);
									  mv.addObject("lista",solicitacaoFantRepository.solicitacoesPorCpf(CPF));
									  mv.addObject("especie",especieRepository.findAll());
				    		          mv.addObject("requisicao",requisicaoRepository.findAll());
				    		          mv.addObject("nome", nome);
				    				  mv.addObject("cpf",CPF);
									  }else {
										  mv.addObject("msg", "Não foi possível salvar porque não está constando usuário");
					    				     mv.addObject("nome", nome);
					    				     mv.addObject("cpf",CPF);
					    				     mv.addObject("lista",solicitacaoFantRepository.solicitacoesPorCpf(CPF));
					    				     mv.addObject("especie",especieRepository.findAll());
					    		             mv.addObject("requisicao",requisicaoRepository.findAll());
									  }
								  }else {
									  mv.addObject("msg", "Deve ser inserido o contato da solciitação");
									  mv.addObject("nome", nome);
				    				  mv.addObject("cpf",CPF);
				    				  mv.addObject("lista",solicitacaoFantRepository.solicitacoesPorCpf(CPF));
				    				  mv.addObject("especie",especieRepository.findAll());
				    		          mv.addObject("requisicao",requisicaoRepository.findAll());
								  }
							  }else {
								  mv.addObject("msg", "deve ser indicada se a requisição e urbana ou para viagem");
								  mv.addObject("nome", nome);
			    				  mv.addObject("cpf",CPF);
			    				  mv.addObject("lista",solicitacaoFantRepository.solicitacoesPorCpf(CPF));
			    				  mv.addObject("especie",especieRepository.findAll());
			    		          mv.addObject("requisicao",requisicaoRepository.findAll());
							  }
						  }else {
							  mv.addObject("msg", "Deve ser preenchido o motivo da solciitação");
							  mv.addObject("nome", nome);
		    				  mv.addObject("cpf",CPF);
		    				  mv.addObject("lista",solicitacaoFantRepository.solicitacoesPorCpf(CPF));
		    				  mv.addObject("especie",especieRepository.findAll());
		    		          mv.addObject("requisicao",requisicaoRepository.findAll());
						  }
					  }else {
						  mv.addObject("msg", "Deve ser definido o destino da solciitação");
						  mv.addObject("nome", nome);
	    				  mv.addObject("cpf",CPF);
	    				  mv.addObject("lista",solicitacaoFantRepository.solicitacoesPorCpf(CPF));
	    				  mv.addObject("especie",especieRepository.findAll());
	    		          mv.addObject("requisicao",requisicaoRepository.findAll());
					  }
				  }else {
					  mv.addObject("msg", "Deve ser escolhido a data final da solciitação");
					  mv.addObject("nome", nome);
 				      mv.addObject("cpf",CPF);
 				      mv.addObject("lista",solicitacaoFantRepository.solicitacoesPorCpf(CPF));
 				      mv.addObject("especie",especieRepository.findAll());
		              mv.addObject("requisicao",requisicaoRepository.findAll());
				  }
			  }else {
				  mv.addObject("msg", "Deve ser escolhido a data inicial da solciitação");
				  mv.addObject("nome", nome);
				  mv.addObject("cpf",CPF);
				  mv.addObject("lista",solicitacaoFantRepository.solicitacoesPorCpf(CPF));
				  mv.addObject("especie",especieRepository.findAll());
		          mv.addObject("requisicao",requisicaoRepository.findAll());
			  }
		  }else {
			  mv.addObject("msg", "Deve ser escolhido o tipo de veículo");
			  mv.addObject("nome", nome);
			  mv.addObject("cpf",CPF);
			  mv.addObject("lista",solicitacaoFantRepository.solicitacoesPorCpf(CPF));
			  mv.addObject("especie",especieRepository.findAll());
	          mv.addObject("requisicao",requisicaoRepository.findAll());
		  }
		  
		  return mv;
		  
		  
	  }
	  @PostMapping("**/verificainseresenha")
	  public ModelAndView VerificaInsereSenha(@RequestParam("digitesenha")String digitesenha,@RequestParam("inseresenha")String inseresenha,
			  @RequestParam("repitasenha") String repitasenha) throws java.text.ParseException {
		  
		  System.out.println("senha: "+ digitesenha);
		  if(digitesenha.equals("")&&digitesenha.equals(null)) {
			  System.out.println("senha de dentro: "+ digitesenha);
			  if(!inseresenha.equals("")||!inseresenha.equals(null)) {
				  
				  if(!repitasenha.equals("")||!repitasenha.equals(null)) {
					  System.out.println("insere= "+inseresenha+" repita= "+repitasenha);
					  if(inseresenha==repitasenha) {
						  ModelAndView mv = new ModelAndView("solicitacao.html");
						  user.setSenha(inseresenha); 
						  usuarioExternoRepository.save(user);
				           mv.addObject("nome", nome);
				           mv.addObject("cpf",CPF);
				           mv.addObject("siglaref", listaorgao);
				           mv.addObject("sigla", null);
				           mv.addObject("lista",solicitacaoFantRepository.solicitacoesPorCpf(CPF));
				           mv.addObject("msg","Preencha a solicitação e clique em Salvar");
				           mv.addObject("especie",especieRepository.findAll());
				           mv.addObject("requisicao",requisicaoRepository.findAll());
				            mv.addObject("verificador", "");
				            mv.addObject("visivel",0);
				            return mv;
					  }else { ModelAndView mv = new ModelAndView("acesso.html");
						     mv.addObject("msg", "As senhas não conferem, digite novamente");
		                     mv.addObject("verificador","externo");
		                     mv.addObject("visivel",1);
		                     
		                     return mv;
					  }
					   
				  }else {ModelAndView mv = new ModelAndView("acesso.html");
					  mv.addObject("msg", "Deve ser preenchido o campo de repetição da senha, que deve ser igual ao da inserção ");
	                     mv.addObject("verificador","externo");
	                     mv.addObject("visivel",1);
					  return mv;
				  }
			  }else { ModelAndView mv = new ModelAndView("acesso.html");
				      mv.addObject("msg", "O campo de inserção de senha está vazio");
	                     mv.addObject("verificador","externo");
	                     mv.addObject("visivel",1);
				      return mv;}
		  }else { 
			  if(ue.getSenha().equals(digitesenha)) {
				  ModelAndView mv = new ModelAndView("solicitacao.html");
		             mv.addObject("nome", ue.getNome());
		             System.out.println("Nome "+ue.getNome()+" cpf "+ue.getCpf()+" cd= "+ue.getId_unidade()+" get senha "+ue.getSenha()+" digite a senha "+digitesenha);
		             mv.addObject("cpf", ue.getCpf());
		             mv.addObject("msg","Preencha a solicitação e clique em Salvar");
		             listaorgao =  (ArrayList<Orgao>) orgaoRepository.pesquisaOrgaoPorSigla(ue.getSigla_unidade());
		             mv.addObject("siglaref", listaorgao);
		             mv.addObject("especie",especieRepository.findAll());
		             mv.addObject("requisicao",requisicaoRepository.findAll());
		             mv.addObject("sigla", ue.getSigla_unidade());
		             mv.addObject("verificador", "");
		             mv.addObject("visivel",0);	
		             mv.addObject("lista",solicitacaoFantRepository.solicitacoesPorCpf(CPF));
		             return mv;
		             
			  }else { ModelAndView mv = new ModelAndView("acesso.html");
			  System.out.println("Onde a senha nao confere--Nome "+ue.getNome()+" cpf "+ue.getCpf()+" cd= "+ue.getId_unidade()+" get senha  "+ue.getSenha()+"  digite a senha "+digitesenha);
				      mv.addObject("msg","Senha não confere, começe novamente");
				      mv.addObject("nome", ue.getNome());
				      mv.addObject("cpf", ue.getCpf());	           
		              listaorgao=new ArrayList<>();
		              mv.addObject("siglaref", listaorgao);
		              mv.addObject("sigla", "");
		              mv.addObject("especie",especieRepository.findAll());
		              mv.addObject("requisicao",requisicaoRepository.findAll());
		              mv.addObject("lista", new SolicitacaoFant());
		              mv.addObject("verificador", "interno");
		              mv.addObject("visivel",1);
		              mv.addObject("vcpf", 1);
		              return mv;
			  }
			  
		  }
		  
		  
		  
	  }

	  
	 

}
