package com.everton.controllers;

import java.util.List;

import java.util.Optional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.everton.dtos.CategoriaDto;
import com.everton.models.CategoriaModel;
import com.everton.repositories.CategoriaRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/Categoria")
public class CategoriaController {
    @Autowired
    CategoriaRepository repositorys;

    @GetMapping("/")
    public String index() {
        return "Categoria/indexCategoria";
    }

    @GetMapping("/inserirCategoria/")
    public String inserir() {
        return "Categoria/inserirCategoria";
    }

    @PostMapping("/inserirCategoria/")
    public String inserirBD(
            @ModelAttribute @Valid CategoriaDto categoriaDto,
            BindingResult result, RedirectAttributes msg) {
        if (result.hasErrors()) {
            msg.addFlashAttribute("erroCategoria", "Erro ao cadastrar categoria");
            return "redirect:/Categoria/inserirCagetoria/";
        }
        var categoriaModel = new CategoriaModel();
        BeanUtils.copyProperties(categoriaDto, categoriaModel);
        repositorys.save(categoriaModel);
        msg.addFlashAttribute("sucessoCadastrarCategoria", "Categoria cadastrada");
        return "redirect:../";
    }

    @GetMapping("/listarCategoria/")
    public ModelAndView listar() {
        ModelAndView mv = new ModelAndView("Categoria/listarCategoria");
        List<CategoriaModel> lista = repositorys.findAll();
        mv.addObject("categorias", lista);
        return mv;
    }

    @GetMapping("/editarCategoria/{Id}")
    public ModelAndView editar(@PathVariable(value = "Id") int Id) {
        ModelAndView mv = new ModelAndView("Categoria/editarCategoria");
        Optional<CategoriaModel> categoria = repositorys.findById(Id);
        mv.addObject("Id", categoria.get().getId());
        mv.addObject("Descripcion", categoria.get().getDescripcion());
        return mv;
    }

    @PostMapping("/editarCategoria/{Id}")
    public String editarBD(
            @ModelAttribute @Valid CategoriaDto categoriaDto,
            BindingResult result, RedirectAttributes msg, @PathVariable(value = "Id") int Id) {
        Optional<CategoriaModel> categoria = repositorys.findById(Id);
        if (result.hasErrors()) {
            msg.addFlashAttribute("erroEditar", "Erro ao editar cadastro");
            return "redirect:/Categoria/listarCategoria/";
        }
        var categoriaModel = categoria.get();
        BeanUtils.copyProperties(categoriaDto, categoriaModel);
        repositorys.save(categoriaModel);
        msg.addFlashAttribute("sucessoEditar", "Categoria editada!");
        return "redirect:../../Categoria/listarCategoria/";
    }

    @GetMapping("/excluirCategoria/{Id}")
    public String excluirCategoria(@PathVariable(value = "Id") int Id) {
        Optional<CategoriaModel> categoria = repositorys.findById(Id);
        if (categoria.isEmpty()) {
            return "redirect:../../Categoria/listarCategoria/";
        }
        repositorys.deleteById(Id);
        return "redirect:../../Categoria/listarCategoria/";
    }

}
