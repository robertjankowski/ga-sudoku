extern crate gio;
extern crate gtk;

use std::process;

use gio::prelude::*;
use gtk::prelude::*;

fn main() {
    if gtk::init().is_err() {
        eprintln!("failed to initialize GTK Application");
        process::exit(1);
    }

    let glade_src = include_str!("sudoku_ui.glade");
    let builder = gtk::Builder::new_from_string(glade_src);
    let window: gtk::Window = builder.get_object("main").unwrap();

    window.show_all();

    gtk::main();
}