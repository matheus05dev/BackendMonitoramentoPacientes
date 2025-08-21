package com.springwalker.back.core.enums;

public enum Diagnostico {
    A41("Septicemia"),
    A02("Infecção por Salmonella, exceto tifo e paratifo"),
    A04("Outras infecções intestinais bacterianas"),
    A08("Infecções virais intestinais"),
    B95("Estreptococos como causa de doenças classificadas em outros capítulos"),
    B96("Outros agentes bacterianos como causa de doenças"),
    E86("Desidratação"),
    G00("Meningite bacteriana"),
    G04("Encefalite, mielite e encefalomielite"),
    J12("Pneumonia viral, não classificada em outra parte"),
    J15("Pneumonia bacteriana, não classificada em outra parte"),
    J22("Infecção respiratória aguda do trato inferior, não especificada"),
    J80("Síndrome do desconforto respiratório agudo"),
    J96("Insuficiência respiratória"),
    K52("Gastroenterite e colite não infecciosas"),
    K92("Outras doenças do aparelho digestivo"),
    N10("Nefrite tubulointersticial aguda"),
    N17("Insuficiência renal aguda"),
    N39_0("Infecção do trato urinário inferior"),
    O99("Outras doenças maternas não classificadas em outra parte"),
    P23("Pneumonia congênita"),
    R11("Náusea e vômito"),
    R13("Disfagia"),
    R55("Síncope e colapso"),
    U07_2("COVID-19, vírus não identificado – diagnóstico clínico ou epidemiológico"),
    S00("Traumatismo superficial da cabeça"),
    S01("Ferimento da cabeça"),
    S02("Fraturas dos ossos do crânio e da face"),
    S03("Luxações, entorses e distensões das articulações e dos ligamentos da cabeça"),
    S04("Traumatismo dos nervos cranianos"),
    S06("Traumatismo intracraniano"),
    S10("Traumatismo superficial do pescoço"),
    S13("Luxações e entorses do pescoço"),
    S20("Traumatismo superficial do tórax"),
    S22("Fratura de costela(s), esterno e coluna torácica"),
    S30("Traumatismo superficial do abdome, parte inferior das costas, região lombar e pelve"),
    S32("Fratura da pelve"),
    S40("Traumatismo superficial do ombro e braço"),
    S42("Fratura do ombro e braço"),
    S43("Luxação do ombro e braço"),
    S50("Traumatismo superficial do cotovelo e antebraço"),
    S52("Fratura do antebraço"),
    S53("Luxação do cotovelo"),
    S60("Traumatismo superficial do punho e mão"),
    S61("Ferimento do punho e da mão"),
    S62("Fratura do punho e mão"),
    S63("Luxações e entorses do punho e da mão"),
    S70("Traumatismo superficial do quadril e coxa"),
    S72("Fratura do fêmur"),
    S73("Luxação do quadril"),
    S80("Traumatismo superficial do joelho e da perna"),
    S82("Fratura da perna, incluindo tornozelo"),
    S83("Luxação do joelho"),
    S90("Traumatismo superficial do tornozelo e pé"),
    S91("Ferimento do tornozelo e do pé"),
    S92("Fratura do pé, exceto tornozelo"),
    S93("Luxações e entorses do tornozelo e pé"),
    T14("Traumatismo de região não especificada do corpo"),
    T81_0("Hemorragia e hematoma como complicação de procedimento, não classificados em outra parte");




    private final String descricao;

    Diagnostico(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
