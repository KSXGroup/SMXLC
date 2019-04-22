
SECTION .text   6

_g__Zstrcatss:
        push    r15
        push    r14
        push    r13
        mov     r13, rdi
        push    r12
        push    rbp
        mov     rbp, rsi
        push    rbx
        sub     rsp, 8
        mov     r14, qword [rdi]
        mov     r12, qword [rsi]
        lea     r15, [r14+r12]
        lea     rdi, [r15+9H]
        call    malloc
        lea     rsi, [r13+8H]
        mov     rdx, r14
        mov     qword [rax], r15
        mov     rbx, rax
        lea     rdi, [rax+8H]
        call    memcpy
        lea     rdi, [rbx+r14+8H]
        lea     rdx, [r12+1H]
        lea     rsi, [rbp+8H]
        call    memcpy
        add     rsp, 8
        mov     rax, rbx
        pop     rbx
        pop     rbp
        pop     r12
        pop     r13
        pop     r14
        pop     r15
        ret


ALIGN   16

_g__Zstrcmpss:
        mov     eax, 1
        mov     rcx, qword [rsi]
        cmp     qword [rdi], rcx
        jg      L_001
        mov     rax, -1
        jl      L_001
        sub     rsp, 8
        add     rsi, 8
        add     rdi, 8
        call    strcmp
        xor     edx, edx
        test    eax, eax
        setne   dl
        neg     rdx
        test    eax, eax
        mov     eax, 1
        cmovle  rax, rdx
        add     rsp, 8
        ret

ALIGN   8
L_001:  ret


ALIGN   8

_g__Zstring4ordi:
        movsx   rax, byte [rdi+rsi+8H]
        ret

ALIGN   16

_g__Zstring4substringii:
        push    r13
        mov     r13, rdi
        push    r12
        movsxd  r12, esi
        push    rbp
        sub     edx, r12d
        push    rbx
        lea     ebx, [rdx+1H]
        add     edx, 10
        movsxd  rdi, edx
        movsxd  rbx, ebx
        sub     rsp, 8
        call    malloc
        lea     rsi, [r13+r12+8H]
        mov     rdx, rbx
        mov     qword [rax], rbx
        mov     rbp, rax
        lea     rdi, [rax+8H]
        call    memcpy
        mov     byte [rbp+rbx+8H], 0
        add     rsp, 8
        mov     rax, rbp
        pop     rbx
        pop     rbp
        pop     r12
        pop     r13
        ret

ALIGN   8

_g__ZgetString:
        push    rbp
        mov     edi, 264
        push    rbx
        sub     rsp, 8
        call    malloc
        lea     rdi, [rel .LC0]
        lea     rbp, [rax+8H]
        mov     rbx, rax
        xor     eax, eax
        mov     rsi, rbp
        call    __isoc99_scanf
        mov     rdx, rbp
L_002:  mov     ecx, dword [rdx]
        add     rdx, 4
        lea     eax, [rcx-1010101H]
        not     ecx
        and     eax, ecx
        and     eax, 80808080H
        jz      L_002
        mov     ecx, eax
        shr     ecx, 16
        test    eax, 8080H
        cmove   eax, ecx
        lea     rcx, [rdx+2H]
        cmove   rdx, rcx
        mov     esi, eax
        add     sil, al
        mov     rax, rbx
        sbb     rdx, 3
        sub     rdx, rbp
        mov     qword [rbx], rdx
        add     rsp, 8
        pop     rbx
        pop     rbp
        ret


_g__ZgetInt:
        sub     rsp, 24
        lea     rdi, [rel .LC1]
        xor     eax, eax
        lea     rsi, [rsp+8H]
        call    __isoc99_scanf
        mov     rax, qword [rsp+8H]
        add     rsp, 24
        ret


ALIGN   16

_g__Zstring4parseInt:
        mov     rax, qword [rdi]
        test    rax, rax
        jle     L_005
        movsx   edx, byte [rdi+8H]
        lea     ecx, [rdx-30H]
        cmp     cl, 9
        ja      L_005
        lea     rcx, [rdi+9H]
        lea     rdi, [rdi+rax+8H]
        xor     eax, eax
        jmp     L_004





ALIGN   8
L_003:  movsx   edx, byte [rcx]
        add     rcx, 1
        lea     esi, [rdx-30H]
        cmp     sil, 9
        ja      L_006
L_004:  sub     edx, 48
        lea     rax, [rax+rax*4]
        movsxd  rdx, edx
        lea     rax, [rdx+rax*2]
        cmp     rdi, rcx
        jnz     L_003
        ret

ALIGN   8
L_005:  xor     eax, eax
L_006:  ret


ALIGN   16

printInt:
        mov     rsi, rdi
        xor     eax, eax
        lea     rdi, [rel .LC1]
        jmp     printf


ALIGN   16

_g__Zprintlns:
        add     rdi, 8
        jmp     puts

ALIGN   8

_g__Zprints:
        lea     rsi, [rdi+8H]
        xor     eax, eax
        lea     rdi, [rel .LC0]
        jmp     printf


ALIGN   16

_g__ZtoStringi:
        push    r12
        push    rbp
        mov     rbp, rdi
        push    rbx
        cmp     rdi, 9
        jle     L_010
        xor     r12d, r12d
        mov     ebx, 1
ALIGN   8
L_007:  mov     rax, rbp
        lea     rbx, [rbx+rbx*4]
        add     r12d, 1
        add     rbx, rbx
        cqo
        idiv    rbx
        cmp     rax, 9
        jg      L_007
        movsx   edi, r12w
        movsx   r12, r12w
        add     edi, 9
        movsxd  rdi, edi
        call    malloc
        mov     qword [rax], r12
        mov     rdi, rax
L_008:  mov     r8, qword 0CCCCCCCCCCCCCCCDH
        xor     ecx, ecx

ALIGN   8
L_009:  mov     rax, rbp
        movsx   rsi, cx
        add     ecx, 1
        cqo
        idiv    rbx
        add     eax, 48
        mov     rbp, rdx
        mov     byte [rdi+rsi+8H], al
        mov     rax, rbx
        mul     r8
        mov     rbx, rdx
        shr     rbx, 3
        test    rbp, rbp
        jnz     L_009
        movsx   rcx, cx
        mov     rax, rdi
        add     rcx, 8
        mov     byte [rdi+rcx], 0
        pop     rbx
        pop     rbp
        pop     r12
        ret

ALIGN   8
L_010:  mov     edi, 9
        call    malloc
        mov     rdi, rax
        mov     qword [rax], 0
        test    rbp, rbp
        jg      L_011
        mov     ecx, 8
        mov     rax, rdi
        mov     byte [rdi+rcx], 0
        pop     rbx
        pop     rbp
        pop     r12
        ret

L_011:
        mov     ebx, 1
        jmp     L_008


SECTION .data   


SECTION .bss    


SECTION .rodata.str1.1 

.LC0:
        db 25H, 73H, 00H

.LC1:
        db 25H, 6CH, 64H, 00H

.LC2:
        db 25H, 6CH, 64H, 2CH, 20H, 25H, 73H, 0AH
        db 00H

.LC3:
        db 25H, 6CH, 64H, 0AH, 00H

.LC4:
        db 25H, 6CH, 64H, 2CH, 25H, 73H, 0AH, 00H