pub mod day10;
pub mod day11;

use std::io;

type Error = Box<dyn std::error::Error>;

fn main() {
    let stdin = io::stdin();
    day11::solve(&mut stdin.lock()).expect("error");
}
