use std::io;

pub fn solve(reader: &mut dyn io::BufRead) -> io::Result<()> {
    let mut line = String::new();
    let mut syntax_error_score = 0;
    let mut completion_scores = Vec::new();
    loop {
        line.clear();
        if reader.read_line(&mut line)? <= 0 {
            break;
        }
        match check(line.trim()) {
            CheckResult::Corrupted(s) => syntax_error_score += s,
            CheckResult::Completion(s) => completion_scores.push(s),
        }
    }
    completion_scores.sort();
    println!("syntax error score: {}", syntax_error_score);
    println!(
        "completion score: {:?}",
        completion_scores[completion_scores.len() / 2]
    );
    Ok(())
}

enum CheckResult {
    Corrupted(i32),
    Completion(i64),
}

fn check(line: &str) -> CheckResult {
    let mut stack = Vec::new();
    for c in line.chars() {
        match c {
            '(' | '[' | '{' | '<' => stack.push(c),
            ')' | ']' | '}' | '>' => {
                let top = stack.pop();
                match (top, c) {
                    (Some('('), ')') | (Some('['), ']') | (Some('{'), '}') | (Some('<'), '>') => {}
                    (_, ')') => {
                        return CheckResult::Corrupted(3);
                    }
                    (_, ']') => {
                        return CheckResult::Corrupted(57);
                    }
                    (_, '}') => {
                        return CheckResult::Corrupted(1197);
                    }
                    (_, '>') => {
                        return CheckResult::Corrupted(25137);
                    }
                    _ => {}
                }
            }
            _ => {}
        }
    }
    let mut score: i64 = 0;
    for c in stack.iter().rev() {
        score *= 5;
        match c {
            '(' => score += 1,
            '[' => score += 2,
            '{' => score += 3,
            '<' => score += 4,
            _ => {}
        }
    }
    CheckResult::Completion(score)
}
