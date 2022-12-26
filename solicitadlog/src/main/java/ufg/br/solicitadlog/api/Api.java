package ufg.br.solicitadlog.api;



import java.lang.reflect.Array;
import java.text.DateFormat;
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
	  String nome; String cpf1;List<String> vinculo0 = null;List<String> categoria0 = null;List<Integer> matricula0 = null;String situacao;Integer ano_ingersso;
	  List<String> unidade_vinculo = null;List<Integer> id_unidade = null;List<String> sigla_unidade = null;String cidade; List<String> sigla_Referencia = null;
	  String access; String scope;String token_type;Integer expire;
	  String unidade; String unidade1; String unidade2;
	  String CPF;
	  String mensagem;
	  int cont=0;
	  String matricula; String matricula1;String matricula2;
	  String categoria;String categoria1; String categoria2;
	  String vinculo;String vinculo1;String vinculo2;
	  String siglaUnidade; String siglaUnidade1; String siglaUnidade2;
	  String situacao1; String situacao2;
	  String nr_lotacao;
	  Long cd;Long cd1; Long cd2;
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
	   /*  mv.addObject("siglaref", listaorgao);
	     mv.addObject("sigla", "");
	     mv.addObject("especie",especieRepository.findAll());
	     mv.addObject("requisicao",requisicaoRepository.findAll());
	     mv.addObject("lista", new SolicitacaoFant());
	     mv.addObject("vcpf",1);*/
	     mv.addObject("cormsg",1);
		 return mv;
	 }
	
	@GetMapping("**/novasolicitacao")
	public ModelAndView NovaSolicitacao() {
		ModelAndView mv = new ModelAndView("solicitacao.html");
  	    mv.addObject("nome", nome);
  	    mv.addObject("cpf",CPF);
  	    mv.addObject("especie",especieRepository.findAll());
        mv.addObject("requisicao",requisicaoRepository.findAll());
        mv.addObject("unidade",unidade);
        mv.addObject("lista", solicitacaoFantRepository.solicitacoesPorCpf(CPF));
        mv.addObject("inserirsol", 1);
        
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
     		mv.addObject("cormsg",1);
     	 }else {
     		    ue=usuarioExternoRepository.verExistenciaLocalDeUsuarioExterno(cpf);
     		    
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
     		             }catch (Exception e) {
		        	       mensagem="Nada foi encontrado para este cpf"+e;
		        	       mv.addObject("msg", mensagem);
		        	       mv.addObject("cormsg",1);
					
				        }
       	   			     
       	   			   //  System.out.println(json);
       	   			     JSONArray array = tudo.getJSONArray("vinculos");
       	   	             nome=(String) json.get("nome");
                         CPF=(String) json.get("cpf");
                         mv.addObject("nome",nome);
                         mv.addObject("cpf",CPF);
                         if (array.length()>0) {
                        	
                        	 int cont=1;
                        	 for(int i=0;i<array.length();i++) {
                        		 org.json.JSONObject f =array.getJSONObject(i);
                        		  if (cont==1) {
                        		 
                        		  
                        		  matricula=f.getString("matricula");
                        		  categoria=f.getString("categoria");
                        		  ano_ingersso=(f.getInt("ano_ingresso"));
                        		  cidade=f.getString("cidade_unidade_vinculo");
                        		  situacao=f.getString("situacao");
                        		  vinculo=f.getString("vinculo");
                        		  siglaUnidade=f.getString("sigla_unidade_vinculo");
                        		  listaorgao =  (ArrayList<Orgao>) orgaoRepository.pesquisaOrgaoPorSigla(f.getString("sigla_unidade_vinculo"));
                        		  cd_orgao=orgaoRepository.buscaCodigoPorSg_orgao(f.getString("sigla_unidade_vinculo"));
                        		  unidade=(f.getString("unidade_vinculo"));
                                  for(Long c:cd_orgao) {
           		            	    cd=(c);           		            	    
           		                  }
                                  cont=cont+1;
                        	      mensagem="Usuario "+json.get("nome");	 
                        	      mv.addObject("msg", mensagem);
                        	      mv.addObject("vunidade",unidade);
                        	      
                        		  }else if (cont==2){
                        			  matricula1=f.getString("matricula");
                            		  categoria1=f.getString("categoria");
                            		  ano_ingersso=(f.getInt("ano_ingresso"));
                            		  cidade=f.getString("cidade_unidade_vinculo");
                            		  situacao1=f.getString("situacao");
                            		  vinculo1=f.getString("vinculo");
                            		  siglaUnidade1=f.getString("sigla_unidade_vinculo");
                            		  listaorgao =  (ArrayList<Orgao>) orgaoRepository.pesquisaOrgaoPorSigla(f.getString("sigla_unidade_vinculo"));
                            		  cd_orgao=orgaoRepository.buscaCodigoPorSg_orgao(f.getString("sigla_unidade_vinculo"));
                            		  unidade1=(f.getString("unidade_vinculo"));
                                      for(Long c:cd_orgao) {
               		            	    cd1=(c);           		            	    
               		                  }
                                      mensagem="Usuario: "+json.get("nome");	 
                            	      mv.addObject("msg", mensagem);
                                      mv.addObject("vunidade1",unidade1);
                        			  cont=cont+1;
                            	    
                        		  }else if (cont==3) {
                        			  matricula2=f.getString("matricula");
                            		  categoria2=f.getString("categoria");
                            		  ano_ingersso=(f.getInt("ano_ingresso"));
                            		  cidade=f.getString("cidade_unidade_vinculo");
                            		  situacao2=f.getString("situacao");
                            		  vinculo2=f.getString("vinculo");
                            		  siglaUnidade2=f.getString("sigla_unidade_vinculo");
                            		  listaorgao =  (ArrayList<Orgao>) orgaoRepository.pesquisaOrgaoPorSigla(f.getString("sigla_unidade_vinculo"));
                            		  cd_orgao=orgaoRepository.buscaCodigoPorSg_orgao(f.getString("sigla_unidade_vinculo"));
                            		  unidade2=(f.getString("unidade_vinculo"));
                                      for(Long c:cd_orgao) {
               		            	    cd2=(c);           		            	    
               		                  }
                                      mensagem="Usuario: "+json.get("nome");	 
                            	      mv.addObject("msg", mensagem);
                                      mv.addObject("vunidade2",unidade2);
                        			  cont=cont+1;
                            	    
                        		  }

                        	 }
                         }else {
                        	 mensagem="Nenhum usuario encontrado com este cpf";
                        	 mv.addObject("msg", mensagem); 
                        	 mv.addObject("cormsg",1);
                         }
       	   			     

     		         Unirest.shutDown();
     		    	
     		 
     	 }
     	 
     	 
         return mv;  
	  }
	  @GetMapping("**/existencia")
	  public ModelAndView VerExistencia(@RequestParam("lotacao")String lotacao) {
		  ModelAndView mv = new ModelAndView("acesso.html");
		  if (situacao!=null) {situacao.trim();}
		  if (situacao1!=null) {situacao1.trim();}
		  if (situacao2!=null) {situacao2.trim();}
		  nr_lotacao=lotacao;
		  
		   ue=(Usuario_Externo) usuarioExternoRepository.verExistenciaLocalDeUsuarioExterno(CPF);
		    if (Objects.isNull(ue)) {
		    	//Aqui comeca a veriicacao externa
		    	if (lotacao.equals("1")) {
		    		if(situacao.equals("Ativo          ")) {
		    			mv.addObject("existe",0);
		    		}else {
		    			mensagem="Usuário não habilitado(a) para solicitação devido a situação: "+situacao;
		    			mv.addObject("cormsg",1);
		    			mv.addObject("nome", "");
		    		    mv.addObject("cpf","");
		    		    mv.addObject("existe",2);
		    		}
		    	}else if (lotacao.equals("2")) {
		    		if (situacao1.equals("Ativo          ")) {
		    			mv.addObject("existe",0);
		    		}else {
		    			mensagem="Usuário não habilitado(a) para solicitação devido a situação: "+situacao1;
		    			mv.addObject("cormsg",1);
		    			mv.addObject("nome", "");
		    		    mv.addObject("cpf","");
		    		    mv.addObject("existe",2);
		    		}
		    	}else if(lotacao.equals("3")) {
		    		if(situacao2.equals("Ativo          ")) {
		    			mv.addObject("existe",0);
		    		}else {
		    			mensagem="Usuário não habilitado(a) para solicitação devido a situação: "+situacao2;
		    			mv.addObject("cormsg",1);
		    			mv.addObject("nome", "");
		    		    mv.addObject("cpf","");
		    		    mv.addObject("existe",2);
		    		}
		    	}else {
		    		mensagem="Usuário não habilitado(a) para solicitação";
		    		mv.addObject("cormsg",1);
		    		mv.addObject("nome", "");
		   	        mv.addObject("cpf","");
		   	        mv.addObject("existe",2);
		    	}
		    	mv.addObject("existe",0);
		    	//Aqui comeca a verificacao interna
		    }else if (lotacao.equals("1")) {
		    	 if (situacao.equals("Ativo          ")) {
		    		 mv.addObject("existe",1);
		    		 
		    	 }else {
		    			mensagem="Usuário não habilitado(a) para solicitação devido a situação: "+situacao;
		    			mv.addObject("cormsg",1);
		    			mv.addObject("nome", "");
		    		    mv.addObject("cpf","");
		    		    mv.addObject("existe",2);
		    	 }
		    	
		    	
		    }else if (lotacao.equals("2")) {
		    	if(situacao1.equals("Ativo          ")) {
		    		mv.addObject("existe",1);
		    	}else {
	    			mensagem="Usuário não habilitado(a) para solicitação devido a situação: "+situacao1;
	    			mv.addObject("cormsg",1);
	    			mv.addObject("nome", "");
	    		    mv.addObject("cpf","");
	    		    mv.addObject("existe",2);
		    	}
		    	
		    }else if (lotacao.equals("3")) {
		    	if(situacao2.equals("Ativo          ")) {
		    		mv.addObject("existe",1);
		    	}else {
	    			mensagem="Usuário não habilitado(a) para solicitação devido a situação: "+situacao2;
	    			mv.addObject("cormsg",1);
	    			mv.addObject("nome", "");
	    		    mv.addObject("cpf","");
	    		    mv.addObject("existe",2);
		    	}
		    	
		    }else {
		    	mensagem="Não pode ser definida uma posição de usuário";
    			mv.addObject("cormsg",1);
    			mv.addObject("nome", "");
    		    mv.addObject("cpf","");
    		    mv.addObject("existe",2);
		    }
		  mv.addObject("msg",mensagem);
		  return mv;
	  }
	  
	  @GetMapping("**/alterasenha")
	  public ModelAndView AlteraSenha(@RequestParam("senha")String senha, @RequestParam("alterasenha")String alterasenha,
			  @RequestParam("repitaalterasenha")String repitaalterasenha) {
		      if(ue.getSenha().equals(senha)) {
		    	  if (alterasenha.equals(repitaalterasenha)) {
		    		 try { 
		    			 ue.setSenha(alterasenha);
		    			
		    		      usuarioExternoRepository.save(ue);
		    		      mensagem="Senha alterada com sucesso";
		    		      ModelAndView mv = new ModelAndView("solicitacao.html");
		            	  mv.addObject("nome", nome);
		            	  mv.addObject("cpf",CPF);
		            	  mv.addObject("especie",especieRepository.findAll());
				          mv.addObject("requisicao",requisicaoRepository.findAll());
				          mv.addObject("unidade",unidade);
				          mv.addObject("msg", mensagem);
				          mv.addObject("lista", solicitacaoFantRepository.solicitacoesPorCpf(CPF));
				          return mv;
		    		 }catch (Exception e) {
						mensagem="Erro ao alterar a senha "+e;
						ModelAndView mv = new ModelAndView("acesso.html");
		            	  mv.addObject("nome", nome);
		            	  mv.addObject("cpf",CPF);
		            	  mv.addObject("msg", mensagem);
		            	  mv.addObject("alterasenha",1);
		            	  return mv;
					}
		    	  }else {
		    		  mensagem="Você digitou duas senhas diferentes";
						ModelAndView mv = new ModelAndView("acesso.html");
		            	  mv.addObject("nome", nome);
		            	  mv.addObject("cpf",CPF);
		            	  mv.addObject("msg", mensagem);
		            	  mv.addObject("alterasenha",1);
		            	  return mv;
		    	  }
		      }else {
		    	  mensagem="Senha incorreta";
					ModelAndView mv = new ModelAndView("acesso.html");
	            	  mv.addObject("nome", nome);
	            	  mv.addObject("cpf",CPF);
	            	  mv.addObject("msg", mensagem);
	            	  mv.addObject("alterasenha",1);
	            	  return mv;
		      }
		  
	  }
	  
	  @GetMapping("/abrialterarsenha")
	  public ModelAndView AbreAlteraSenha() {
		  ModelAndView mv = new ModelAndView("acesso.html");
		  mv.addObject("nome", nome);
    	  mv.addObject("cpf",CPF);
    	  mv.addObject("alterasenha",1);
    	  
    	  return mv;
	  }
	  
	  @PostMapping("**/inserirusuario")
	  public ModelAndView InserirUsuario(@RequestParam("inseresenha")String inseresenha, @RequestParam("repitasenha")String repitasenha,
			  @RequestParam("email")String email, @RequestParam("dtnascimento")String dtnascimento) throws java.text.ParseException {
		ModelAndView mv1 = new ModelAndView();
		Usuario_Externo u = new Usuario_Externo();
		
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		if (!dtnascimento.isEmpty()&&!dtnascimento.equals("")) {
		  if(inseresenha.equals(repitasenha)) {
			  u.setNome(nome);
			  u.setCpf(CPF);
			  u.setEmail(email);
			  u.setNascimento(sd.parse(dtnascimento));
			  
              if (nr_lotacao.equals("1")) {
            	  u.setCategoria(categoria);
            	  u.setAno_ingresso(ano_ingersso);
            	  u.setCidade_vinculo(cidade);
            	  u.setId_unidade(cd);
            	  u.setMatricula(matricula);
            	  u.setSenha(repitasenha);
            	  u.setSigla_unidade(siglaUnidade);
            	  u.setSituacao(situacao);
            	  u.setUnidade_vinculo(unidade);
            	  u.setVinculo(vinculo);
            	  usuarioExternoRepository.save(u);
            	  ModelAndView mv = new ModelAndView("solicitacao.html");
            	  mv.addObject("nome", nome);
            	  mv.addObject("cpf",CPF);
            	  mv.addObject("especie",especieRepository.findAll());
		          mv.addObject("requisicao",requisicaoRepository.findAll());
		          mv.addObject("unidade",unidade);
		          mv.addObject("lista", solicitacaoFantRepository.solicitacoesPorCpf(CPF));
            	  return mv;
              }else if (nr_lotacao.equals("2")) {
            	  u.setCategoria(categoria1);
            	  u.setAno_ingresso(ano_ingersso);
            	  u.setCidade_vinculo(cidade);
            	  u.setId_unidade(cd1);
            	  u.setMatricula(matricula1);
            	  u.setSenha(repitasenha);
            	  u.setSigla_unidade(siglaUnidade1);
            	  u.setSituacao(situacao1);
            	  u.setUnidade_vinculo(unidade1);
            	  u.setVinculo(vinculo1);
            	  usuarioExternoRepository.save(u);
            	  ModelAndView mv = new ModelAndView("solicitacao.html");
            	  mv.addObject("nome", nome);
            	  mv.addObject("cpf",CPF);
            	  mv.addObject("especie",especieRepository.findAll());
		          mv.addObject("requisicao",requisicaoRepository.findAll());
		          mv.addObject("unidade",unidade1);
		          mv.addObject("lista", solicitacaoFantRepository.solicitacoesPorCpf(CPF));
            	  return mv;
              }else if (nr_lotacao.equals("3")) {
            	  u.setCategoria(categoria2);
            	  u.setAno_ingresso(ano_ingersso);
            	  u.setCidade_vinculo(cidade);
            	  u.setId_unidade(cd2);
            	  u.setMatricula(matricula2);
            	  u.setSenha(repitasenha);
            	  u.setSigla_unidade(siglaUnidade2);
            	  u.setSituacao(situacao2);
            	  u.setUnidade_vinculo(unidade2);
            	  u.setVinculo(vinculo2);
            	  usuarioExternoRepository.save(u); 
            	  ModelAndView mv = new ModelAndView("solicitacao.html");
            	  mv.addObject("nome", nome);
            	  mv.addObject("cpf",CPF);
            	  mv.addObject("especie",especieRepository.findAll());
		          mv.addObject("requisicao",requisicaoRepository.findAll());
		          mv.addObject("unidade",unidade2);
		          mv.addObject("lista", solicitacaoFantRepository.solicitacoesPorCpf(CPF));
            	  return mv;
              }
		  }else {
			  mensagem="Senhas diferentes, por favor digite senhas iguais nos dois campos";
			  ModelAndView mv = new ModelAndView("acesso.html");
			  mv.addObject("existe",0);
			  mv.addObject("msg",mensagem);
			  return mv;
		  }
		}else {
			mensagem="Data de nascimento vazia, ela e necessária";
			  ModelAndView mv = new ModelAndView("acesso.html");
			  mv.addObject("existe",0);
			  mv.addObject("msg",mensagem);
			  return mv;
		}
		  return mv1;
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
				    				  mv.addObject("inserirsol", null);
									  }else {
										  mv.addObject("msg", "Não foi possível salvar porque não está constando usuário");
					    				     mv.addObject("nome", nome);
					    				     mv.addObject("cpf",CPF);
					    				     mv.addObject("lista",solicitacaoFantRepository.solicitacoesPorCpf(CPF));
					    				     mv.addObject("especie",especieRepository.findAll());
					    		             mv.addObject("requisicao",requisicaoRepository.findAll());
					    		             mv.addObject("inserirsol", null);
									  }
								  }else {
									  mv.addObject("msg", "Deve ser inserido o contato da solciitação");
									  mv.addObject("nome", nome);
				    				  mv.addObject("cpf",CPF);
				    				  mv.addObject("lista",solicitacaoFantRepository.solicitacoesPorCpf(CPF));
				    				  mv.addObject("especie",especieRepository.findAll());
				    		          mv.addObject("requisicao",requisicaoRepository.findAll());
				    		          mv.addObject("inserirsol",1);
								  }
							  }else {
								  mv.addObject("msg", "deve ser indicada se a requisição e urbana ou para viagem");
								  mv.addObject("nome", nome);
			    				  mv.addObject("cpf",CPF);
			    				  mv.addObject("lista",solicitacaoFantRepository.solicitacoesPorCpf(CPF));
			    				  mv.addObject("especie",especieRepository.findAll());
			    		          mv.addObject("requisicao",requisicaoRepository.findAll());
			    		          mv.addObject("inserirsol", 1);
							  }
						  }else {
							  mv.addObject("msg", "Deve ser preenchido o motivo da solciitação");
							  mv.addObject("nome", nome);
		    				  mv.addObject("cpf",CPF);
		    				  mv.addObject("lista",solicitacaoFantRepository.solicitacoesPorCpf(CPF));
		    				  mv.addObject("especie",especieRepository.findAll());
		    		          mv.addObject("requisicao",requisicaoRepository.findAll());
		    		          mv.addObject("inserirsol", 1);
						  }
					  }else {
						  mv.addObject("msg", "Deve ser definido o destino da solciitação");
						  mv.addObject("nome", nome);
	    				  mv.addObject("cpf",CPF);
	    				  mv.addObject("lista",solicitacaoFantRepository.solicitacoesPorCpf(CPF));
	    				  mv.addObject("especie",especieRepository.findAll());
	    		          mv.addObject("requisicao",requisicaoRepository.findAll());
	    		          mv.addObject("inserirsol", 1);
					  }
				  }else {
					  mv.addObject("msg", "Deve ser escolhido a data final da solciitação");
					  mv.addObject("nome", nome);
 				      mv.addObject("cpf",CPF);
 				      mv.addObject("lista",solicitacaoFantRepository.solicitacoesPorCpf(CPF));
 				      mv.addObject("especie",especieRepository.findAll());
		              mv.addObject("requisicao",requisicaoRepository.findAll());
		              mv.addObject("inserirsol", 1);
				  }
			  }else {
				  mv.addObject("msg", "Deve ser escolhido a data inicial da solciitação");
				  mv.addObject("nome", nome);
				  mv.addObject("cpf",CPF);
				  mv.addObject("lista",solicitacaoFantRepository.solicitacoesPorCpf(CPF));
				  mv.addObject("especie",especieRepository.findAll());
		          mv.addObject("requisicao",requisicaoRepository.findAll());
		          mv.addObject("inserirsol", 1);
			  }
		  }else {
			  mv.addObject("msg", "Deve ser escolhido o tipo de veículo");
			  mv.addObject("nome", nome);
			  mv.addObject("cpf",CPF);
			  mv.addObject("lista",solicitacaoFantRepository.solicitacoesPorCpf(CPF));
			  mv.addObject("especie",especieRepository.findAll());
	          mv.addObject("requisicao",requisicaoRepository.findAll());
	          mv.addObject("inserirsol", 1);
		  }
		  
		  return mv;
		  
		  
	  }

      @GetMapping("/recuperarsenha")
	  public ModelAndView RecuperarSenha(@RequestParam("dtnascimentor")String dtnascimentor, @RequestParam("emailr")String emailr) {
             ModelAndView mv = new ModelAndView("acesso.html");
             if(!dtnascimentor.isEmpty()||!dtnascimentor.equals("")) {
            	 if(!emailr.isEmpty()||!emailr.equals("")) {
            		 ue=usuarioExternoRepository.verExistenciaLocalDeUsuarioExterno(CPF);
            		 DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            		 String nascimento = df.format(ue.getNascimento());
            		 if(emailr.equals(ue.getEmail())&&dtnascimentor.equals(nascimento)) {
            			 mensagem="Sua senha é:  "+ue.getSenha();
            			 mv.addObject("msg", mensagem); 
            			 mv.addObject("complementamsg", "Agora click em reiniciar e entre com seu cpf e senha");
            			
            		 }else {
            			 mv.addObject("msg","Dados inseridos não conferem");
            		 }
            	 }else {
            		 mv.addObject("msg","Dados de email estão vazios");
            	 }
             }else {
            	 mv.addObject("msg","Data de nascimento esta vazio");
             }
             
             mv.addObject("ars",null);
			 return mv;
			}
      @GetMapping("/abrirrecuperarsenha")
	  public ModelAndView AbrirRecuperarSenha() {
             ModelAndView mv = new ModelAndView("acesso.html");
             mv.addObject("ars",1);
			 return mv;
			}
	  @PostMapping("**/verificasenha")
	  public ModelAndView VerificaSenha(@RequestParam("digitesenha")String digitesenha) throws java.text.ParseException {
		ModelAndView mv1 = new ModelAndView();
		  
		  
		  ue=usuarioExternoRepository.verExistenciaLocalDeUsuarioExterno(CPF);
		  if (cont<4) {
		  if (digitesenha.equals(ue.getSenha())) {
			  ModelAndView mv = new ModelAndView("solicitacao.html");
        	  mv.addObject("nome", nome);
        	  mv.addObject("cpf",CPF);
        	  mv.addObject("especie",especieRepository.findAll());
	          mv.addObject("requisicao",requisicaoRepository.findAll());
	          Usuario_Externo u = new Usuario_Externo();
	          if (nr_lotacao.equals("1")) {
	        	  u.setCpf(CPF);
	        	  u.setNome(nome);
	        	  u.setNascimento(ue.getNascimento());
	        	  u.setEmail(ue.getEmail());
            	  u.setCategoria(categoria);
            	  u.setAno_ingresso(ano_ingersso);
            	  u.setCidade_vinculo(cidade);
            	  u.setId_unidade(cd);
            	  u.setMatricula(matricula);
            	  u.setSenha(digitesenha);
            	  u.setSigla_unidade(siglaUnidade);
            	  u.setSituacao(situacao);
            	  u.setUnidade_vinculo(unidade);
            	  u.setVinculo(vinculo);
            	  usuarioExternoRepository.save(u);
            	
            	  mv.addObject("nome", nome);
            	  mv.addObject("cpf",CPF);
            	  mv.addObject("especie",especieRepository.findAll());
		          mv.addObject("requisicao",requisicaoRepository.findAll());
		          mv.addObject("unidade",unidade);
		          mv.addObject("lista", solicitacaoFantRepository.solicitacoesPorCpf(CPF));
            	  return mv;
              }else if (nr_lotacao.equals("2")) {
            	  u.setCpf(CPF);
            	  u.setNome(nome);
            	  u.setNascimento(ue.getNascimento());
	        	  u.setEmail(ue.getEmail());
            	  u.setCategoria(categoria1);
            	  u.setAno_ingresso(ano_ingersso);
            	  u.setCidade_vinculo(cidade);
            	  u.setId_unidade(cd1);
            	  u.setMatricula(matricula1);
            	  u.setSenha(digitesenha);
            	  u.setSigla_unidade(siglaUnidade1);
            	  u.setSituacao(situacao1);
            	  u.setUnidade_vinculo(unidade1);
            	  u.setVinculo(vinculo1);
            	  usuarioExternoRepository.save(u);
            	
            	  mv.addObject("nome", nome);
            	  mv.addObject("cpf",CPF);
            	  mv.addObject("especie",especieRepository.findAll());
		          mv.addObject("requisicao",requisicaoRepository.findAll());
		          mv.addObject("unidade",unidade1);
		          mv.addObject("lista", solicitacaoFantRepository.solicitacoesPorCpf(CPF));
            	  return mv;
              }else if (nr_lotacao.equals("3")) {
            	  u.setCpf(CPF);
            	  u.setNome(nome);
            	  u.setNascimento(ue.getNascimento());
	        	  u.setEmail(ue.getEmail());
            	  u.setCategoria(categoria2);
            	  u.setAno_ingresso(ano_ingersso);
            	  u.setCidade_vinculo(cidade);
            	  u.setId_unidade(cd2);
            	  u.setMatricula(matricula2);
            	  u.setSenha(digitesenha);
            	  u.setSigla_unidade(siglaUnidade2);
            	  u.setSituacao(situacao2);
            	  u.setUnidade_vinculo(unidade2);
            	  u.setVinculo(vinculo2);
            	  usuarioExternoRepository.save(u); 
           
            	  mv.addObject("nome", nome);
            	  mv.addObject("cpf",CPF);
            	  mv.addObject("especie",especieRepository.findAll());
		          mv.addObject("requisicao",requisicaoRepository.findAll());
		          mv.addObject("unidade",unidade2);
		          mv.addObject("lista", solicitacaoFantRepository.solicitacoesPorCpf(CPF));
            	  return mv;
              } 
        	  return mv;
			  
		  }else {			  
			  mensagem="Senha incorreta!";
			  ModelAndView mv = new ModelAndView("acesso.html");
        	  mv.addObject("nome", nome);
        	  mv.addObject("cpf",CPF);
			  mv.addObject("existe",1);
			  mv.addObject("msg",mensagem);
			  mv.addObject("cormsg",null);
			  cont=cont+1;
			 
			  return mv;
			  
		  }
		  }else {
			  ModelAndView mv = new ModelAndView("acesso.html");
			     mv.addObject("nome", "");
			     mv.addObject("cpf","");
			     mv.addObject("msg","Reiniciando o login");
			     mv.addObject("cormsg",1);
			     mv.addObject("existe",3);
			     cont=0;
			     return mv;
		  }  
		  
	  }



	 

}
