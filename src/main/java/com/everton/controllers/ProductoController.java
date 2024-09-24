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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.everton.dtos.ProductoDto;
import com.everton.models.ProductoModel;
import com.everton.repositories.ProductoRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/Producto")
public class ProductoController {
    @Autowired
    ProductoRepository repositorys;

    @GetMapping("/")
    public String index() {
        return "Producto/indexProducto";
    }

    @GetMapping("/inserirProducto/")
    public String inserir() {
        return "Producto/inserirProducto";
    }

    @PostMapping("/inserirProducto/")
    public String inserirBD(
            @ModelAttribute @Valid ProductoDto productoDto,
            BindingResult result, RedirectAttributes msg) {
        if (result.hasErrors()) {
            msg.addFlashAttribute("erroProducto", "Erro ao cadastrar producto");
            return "redirect:/Producto/inserirProducto/";
        }
        var productoModel = new ProductoModel();
        BeanUtils.copyProperties(productoDto, productoModel);
        repositorys.save(productoModel);
        msg.addFlashAttribute("sucessoCadastrarProducto", "Producto cadastrado");
        return "redirect:../";
    }

    @GetMapping("/listarProducto/")
    public ModelAndView listar() {
        ModelAndView mv = new ModelAndView("Producto/listarProducto");
        List<ProductoModel> lista = repositorys.findAll();
        mv.addObject("productos", lista);
        return mv;
    }

    @GetMapping("/editarProducto/{Id}")
    public ModelAndView editar(@PathVariable(value = "Id") int Id) {
        ModelAndView mv = new ModelAndView("Producto/editarProducto");
        Optional<ProductoModel> producto = repositorys.findById(Id);
        mv.addObject("Id", producto.get().getId());
        mv.addObject("Nome", producto.get().getNome());
        mv.addObject("Preco", producto.get().getPreco());
        mv.addObject("Categoria_id", producto.get().getCategoria());
        return mv;
    }

    @PostMapping("/editarProducto/{Id}")
    public String editarBD(
            @ModelAttribute @Valid ProductoDto productoDto,
            BindingResult result, RedirectAttributes msg, @PathVariable(value = "Id") int Id) {
        Optional<ProductoModel> producto = repositorys.findById(Id);
        if (result.hasErrors()) {
            msg.addFlashAttribute("erroEditar", "Erro ao editar producto");
            return "redirect:/Producto/listarProducto";
        }
        var productoModel = producto.get();
        BeanUtils.copyProperties(productoDto, productoModel);
        repositorys.save(productoModel);
        msg.addFlashAttribute("sucessoEditar", "Producto editado!");
        return "redirect:../../Producto/listarProducto/";
    }

    @GetMapping("/excluirProducto/{Id}")
    public String excluirProducto(@PathVariable(value = "Id") int Id) {
        Optional<ProductoModel> producto = repositorys.findById(Id);
        if (producto.isEmpty()) {
            return "redirect:../../Producto/listarProducto/";
        }
        repositorys.deleteById(Id);
        return "redirect:../../Producto/listarProducto/";
    }
}
